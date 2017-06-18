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
public class DAOMktsSummit implements DAOMkts {

    private SWrapEToolKit etkWrap;
    private SWrapDOM domWrapper;
    private SWrapJSON sJson;
    private SWrapFile sFile;

    public DAOMktsSummit(SWrapEToolKit etkWrap, String documentPath) {
        this.etkWrap = etkWrap;

        domWrapper = new SWrapDOM();
        sJson = new SWrapJSON();
        sFile = new SWrapFile(documentPath);
    }

    @Override
    public Map<String, IdMkt> getMktIds(Map<String, Trade> trades) throws IOException, JsonProcessingException {
        Map<String, IdMkt> mktIds = new HashMap<>();

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
    public Map<String, Mkts> getMkts(Map<String, IdMkt> mktIds, String curveId, String asOfDate) throws IOException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        try {

            Map<String, Mkts> mkts = new HashMap<>();

            Map<String, String> entitiesZero = new HashMap<>();
            Map<String, String> entitiesCOMM = new HashMap<>();
            for (Map.Entry<String, IdMkt> mktId : mktIds.entrySet()) {
                if ( mktId.getValue().getClassId() == "IdMktIR" ) {
                    entitiesZero.put(mktId.getKey(), mktId.getValue().getRequest(curveId, asOfDate));
                }

                if ( mktId.getValue().getClassId() == "IdMktFXRate" ||
                        mktId.getValue().getClassId() == "IdMktFXRate") {
                    entitiesCOMM.put(mktId.getKey(), mktId.getValue().getRequest(curveId, asOfDate));
                }
            }

            int index = 0;
            List<SWrapEToolKit.ETKResponse> mktsStr = etkWrap.execute("s_market::GetZeroCurve", entitiesZero);
            for (SWrapEToolKit.ETKResponse mktResp : mktsStr ){
                Map<String, String> points = domWrapper.convertZeroResult(mktResp.getResponse());

                IdMkt idMkt = mktIds.get(mktResp.getId());
                MktsZeroCurve mkt = new MktsZeroCurve((IdMktIR) idMkt, points);
                mkts.put(mktResp.getId(), mkt);

                sFile.writeStringToFile(idMkt.getId(curveId, asOfDate) + ".json",
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

    public Map<String, IdMkt> addZeroMkt(Map<String, IdMkt> mktIds, JsonNode assetJson) {
        String ccy = assetJson.path("Ccy").asText();
        String dmIndex = assetJson.path("INTEREST_dmIndex").asText();
        String subIndex = assetJson.path("SubIndex").asText();

        if ( !dmIndex.equals("FIXED") ) {
            mktIds = addMkt(mktIds, new IdMktIR(ccy, dmIndex));
        }
        mktIds = addMkt(mktIds, new IdMktIR(ccy, subIndex));

        return mktIds;
    }

    public Map<String, IdMkt> addFXRateMkt(Map<String, IdMkt> mktIds, JsonNode asset1Json, JsonNode asset2Json) {
        String ccy1 = asset1Json.path("Ccy").asText();
        String ccy2 = asset1Json.path("Ccy").asText();

        mktIds = addMkt(mktIds, new IdMktFXRate(ccy1, ccy2));

        return mktIds;
    }

    public Map<String, IdMkt> addFXVolMkt(Map<String, IdMkt> mktIds, JsonNode asset1Json, JsonNode asset2Json) {
        String ccy1 = asset1Json.path("Ccy").asText();
        String ccy2 = asset1Json.path("Ccy").asText();

        mktIds = addMkt(mktIds, new IdMktFXVol(ccy1, ccy2));

        return mktIds;
    }

    public Map<String, IdMkt> addMkt(Map<String, IdMkt> mktIds, IdMkt mkt){
        boolean found = false;
        for (Map.Entry<String, IdMkt> it : mktIds.entrySet() ){
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
