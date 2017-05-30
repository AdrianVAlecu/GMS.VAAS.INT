package com.gms.datasource.summit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.gms.datasource.Mkt;
import com.gms.datasource.MktId;
import com.gms.datasource.MktsDAO;
import com.gms.datasource.Trade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gms on 5/30/2017.
 */
public class SummitMktsDAO implements MktsDAO {

    @Override
    public List<MktId> getMktIds(List<Trade> trades) throws IOException, JsonProcessingException {
        List<MktId> mktIds = new ArrayList<>();

        for(Trade trade : trades){
            JsonNode tradeJson = trade.getTradeJSON();
            if ( tradeJson.has("MM") ){
                
            }
        }

        return mktIds;
    }

    @Override
    public List<Mkt> getMkts(String mktsIds, String curveId, String asOfDate) throws IOException, JsonProcessingException {
        List<Mkt> mkts = new ArrayList<>();

        return mkts;
    }
}
