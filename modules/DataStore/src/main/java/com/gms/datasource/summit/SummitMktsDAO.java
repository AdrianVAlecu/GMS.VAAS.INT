package com.gms.datasource.summit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.*;
import summit.etkapi_ws.SU_eToolkitAPIException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.*;

/**
 * Created by gms on 5/30/2017.
 */
public class SummitMktsDAO implements MktsDAO {

    private EToolKitWrapper etkWrap;
    private String documentPath;
    private SummitDOMWrapper domWrapper;

    public SummitMktsDAO(EToolKitWrapper etkWrap, String documentPath) {
        this.etkWrap = etkWrap;
        this.documentPath = documentPath;
        domWrapper = new SummitDOMWrapper();
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
                    mktIds = addMkt(mktIds, new IRMktId(ccy, dmIndex));
                }
                mktIds = addMkt(mktIds, new IRMktId(ccy, subIndex));
            }
        }

        return mktIds;
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
            for (String mktStr : mktsStr ){
                Map<String, String> points = domWrapper.convertZeroResult(mktStr);
                IRMkt mkt = new IRMkt((IRMktId)mktIds.get(index), points);
                index ++;
                mkts.add(mkt);
            }

            return mkts;
        }catch (SU_eToolkitAPIException e){
            throw new RuntimeException(e);
        }
        finally
        {
        }

    }
}
