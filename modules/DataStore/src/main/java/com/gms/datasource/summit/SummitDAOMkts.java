package com.gms.datasource.summit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.*;
import summit.etkapi_ws.SU_eToolkitAPIException;

import java.io.IOException;
import java.util.*;

/**
 * Created by gms on 5/30/2017.
 */
public class SummitDAOMkts implements DAOMkts {

    private SWrapEToolKit etkWrap;
    private SWrapDOM domWrapper;
    private SWrapJSON sJson;
    private SWrapFile sFile;

    public SummitDAOMkts(SWrapEToolKit etkWrap, String documentPath) {
        this.etkWrap = etkWrap;

        domWrapper = new SWrapDOM();
        sJson = new SWrapJSON();
        sFile = new SWrapFile(documentPath);
    }

    @Override
    public Map<String, MktId> getMktIds(Map<String, Trade> trades) throws IOException, JsonProcessingException {
        Map<String, MktId> mktIds = new HashMap<>();

        for(Map.Entry<String, Trade> trade : trades.entrySet()){
            JsonNode tradeJson = trade.getValue().getTradeJSON();
            if ( tradeJson.has("MM") ){
                JsonNode assetJson = tradeJson.path("MM").path("Assets").path("ASSET1");

                addZeroMkt(mktIds, assetJson);
            }

            if (tradeJson.has("SWAP")){
                JsonNode asset1Json = tradeJson.path("SWAP").path("Assets").path("ASSET1");
                JsonNode asset2Json = tradeJson.path("SWAP").path("Assets").path("ASSET2");

                addZeroMkt(mktIds, asset1Json);
                addZeroMkt(mktIds, asset2Json);
                addFXRateMkt(mktIds, asset1Json, asset2Json);
                addFXVolMkt(mktIds, asset1Json, asset2Json);
            }
        }

        return mktIds;
    }



    @Override
    public Map<String, Mkt> getMkts(Map<String, MktId> mktIds, String curveId, String asOfDate) throws IOException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        try {

            Map<String, Mkt> mkts = new HashMap<>();

            Map<String, String> entitiesZero = new HashMap<>();
            Map<String, String> entitiesCOMM = new HashMap<>();
            for (Map.Entry<String, MktId> mktId : mktIds.entrySet()) {
                if ( mktId.getValue().getClassId() == "MktIdIR" ) {
                    entitiesZero.put(mktId.getKey(), mktId.getValue().getRequest(curveId, asOfDate));
                }

                if ( mktId.getValue().getClassId() == "MktIdFXRate" ||
                        mktId.getValue().getClassId() == "MktIdFXRate") {
                    entitiesCOMM.put(mktId.getKey(), mktId.getValue().getRequest(curveId, asOfDate));
                }
            }

            int index = 0;
            List<SWrapEToolKit.ETKResponse> mktsStr = etkWrap.execute("s_market::GetZeroCurve", entitiesZero);
            for (SWrapEToolKit.ETKResponse mktResp : mktsStr ){
                Map<String, String> points = domWrapper.convertZeroResult(mktResp.getResponse());

                MktId mktId = mktIds.get(mktResp.getId());
                MktZeroCurve mkt = new MktZeroCurve((MktIdIR)mktId, points);
                mkts.put(mktResp.getId(), mkt);

                sFile.writeStringToFile(mktId.getId(curveId, asOfDate) + ".json",
                        sJson.writeJSON(mkt));
            }

            return mkts;
        }catch (SU_eToolkitAPIException e){
            throw new RuntimeException(e);
        }
        finally
        {
        }

    }

    public Map<String, MktId> addZeroMkt(Map<String, MktId> mktIds, JsonNode assetJson) {
        String ccy = assetJson.path("Ccy").asText();
        String dmIndex = assetJson.path("INTEREST_dmIndex").asText();
        String subIndex = assetJson.path("SubIndex").asText();

        if ( !dmIndex.equals("FIXED") ) {
            mktIds = addMkt(mktIds, new MktIdIR(ccy, dmIndex));
        }
        mktIds = addMkt(mktIds, new MktIdIR(ccy, subIndex));

        return mktIds;
    }

    public Map<String, MktId> addFXRateMkt(Map<String, MktId> mktIds, JsonNode asset1Json, JsonNode asset2Json) {
        String ccy1 = asset1Json.path("Ccy").asText();
        String ccy2 = asset1Json.path("Ccy").asText();

        mktIds = addMkt(mktIds, new MktIdFXRate(ccy1, ccy2));

        return mktIds;
    }

    public Map<String, MktId> addFXVolMkt(Map<String, MktId> mktIds, JsonNode asset1Json, JsonNode asset2Json) {
        String ccy1 = asset1Json.path("Ccy").asText();
        String ccy2 = asset1Json.path("Ccy").asText();

        mktIds = addMkt(mktIds, new MktIdFXVol(ccy1, ccy2));

        return mktIds;
    }

    public Map<String, MktId> addMkt(Map<String, MktId> mktIds, MktId mkt){
        boolean found = false;
        for (Map.Entry<String, MktId> it : mktIds.entrySet() ){
            if ( it.getValue().getRequest("", "").equals(mkt.getRequest("", "")) ){
                found = true;
                break;
            }
        }

        if ( ! found ) {
            mktIds.put(mkt.getId("", ""), mkt);
        }

        return mktIds;
    }
}
