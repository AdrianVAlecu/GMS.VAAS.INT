package com.gms.datasource.files;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gms.datasource.DAOTrades;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;

public class FilesDAOTrades implements DAOTrades {
    	public Map<String, TradeId> getTradeIds(String query) throws IOException, JsonProcessingException{
		
		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT TradeType, TradeId, TradeVersion from SummitTradeData where " + query;
		
		try
		{
			Map<String, TradeId> tradeIds = new HashMap<>();
			TradeId tradeId = new TradeId("TradeType","TradeId",1);
			tradeIds.put(tradeId.getId(), tradeId);

			ObjectMapper mapper = new ObjectMapper();
			return tradeIds;
		}
		catch(RuntimeException e)
		{
		    throw e;
	    }
		finally 
		{
		}
	}
	
	public Map<String, Trade> getTrades(Map<String, TradeId> tradeIds) throws IOException, JsonProcessingException{
		
		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT TradeXML from SummitTradeData where TradeType = ? and TradeId = ? and TradeVersion = ?";
		
		try
		{
			Map<String, Trade> trades = new HashMap<>();

			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);

			/// TBDAA - execute this in batchs of 1000
			for (Map.Entry<String, TradeId> tradeIdEntry: tradeIds.entrySet() ) {

				TradeId tradeId = tradeIdEntry.getValue();
    			StringBuffer outXMLResponse = new StringBuffer();
    			Vector<StringBuffer> messageList = new Vector<StringBuffer>();

                String tradeSQL = "SELECT TradeJSON from SummitTradeData where TradeType = " + tradeId.getTradeType() + 
                                                    " and TradeId = " + tradeId.getTradeId() +
                                                    " and TradeVersion = " + tradeId.getTradeVersion();

				JsonNode tradeJson = jsonMapper.readTree("TradeJSON");
				Trade trade = new Trade(tradeId, tradeJson);
				trades.put(tradeIdEntry.getKey(), trade);
			}
			
			return trades;
		}
		catch(RuntimeException e)
		{
		    throw e;
	    }
		finally 
		{
		}

	}

}
