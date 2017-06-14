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
public class SummitMktsDAO implements MktsDAO {

    private SWrapEToolKit etkWrap;
    private SWrapDOM domWrapper;
    private SWrapJSON sJson;
    private SWrapFile sFile;

    public SummitMktsDAO(SWrapEToolKit etkWrap, String documentPath) {
        this.etkWrap = etkWrap;

        domWrapper = new SWrapDOM();
        sJson = new SWrapJSON();
        sFile = new SWrapFile(documentPath);
    }

    @Override
    public List<MktId> getMktIds(List<Trade> trades) throws IOException, JsonProcessingException {
        List<MktId> mktIds = new ArrayList<>();

        for(Trade trade : trades){
            JsonNode tradeJson = trade.getTradeJSON();
            if ( tradeJson.has("MM") ){
                JsonNode assetJson = tradeJson.path("MM").path("Assets").path("ASSET1");
                String ccy = assetJson.path("Ccy").asText();
                String dmIndex = assetJson.path("INTEREST_dmIndex").asText();
                String subIndex = assetJson.path("SubIndex").asText();

                if ( !dmIndex.equals("FIXED") ) {
                    mktIds = addMkt(mktIds, new MktIdIR(ccy, dmIndex));
                }
                mktIds = addMkt(mktIds, new MktIdIR(ccy, subIndex));
            }
        }

        return mktIds;
    }

    @Override
    public List<Mkt> getMkts(List<MktId> mktIds, String curveId, String asOfDate) throws IOException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        try {

            List<Mkt> mkts = new Vector<>();

            Vector<String> entities = new Vector<>();
            for (MktId mktId : mktIds) {
                entities.add(mktId.getRequest(curveId, asOfDate));
            }
            int index = 0;
            Vector<String> mktsStr = etkWrap.execute("s_market::GetZeroCurve", entities);
            for (MktId mktId : mktIds ){
                String mktStr = mktsStr.get(index);
                Map<String, String> points = domWrapper.convertZeroResult(mktsStr.get(index));

                MktZeroCurve mkt = new MktZeroCurve((MktIdIR)mktIds.get(index), points);
                index ++;
                mkts.add(mkt);

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


    public List<MktId> addMkt(List<MktId> mkts, MktId mkt){
        boolean found = false;
        for (MktId it : mkts ){
            if ( it.getRequest("", "").equals(mkt.getRequest("", "")) ){
                found = true;
                break;
            }
        }

        if ( ! found ) {
            mkts.add(mkt);
        }

        return mkts;
    }
}
