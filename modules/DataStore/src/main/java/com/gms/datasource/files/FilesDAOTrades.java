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
import com.gms.datasource.IdTrade;

public class FilesDAOTrades implements DAOTrades {
    	public Map<String, IdTrade> getTradeIds(String query) throws IOException, JsonProcessingException{
		
		/// the database context is IdTrade, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT TradeType, IdTrade, TradeVersion from SummitTradeData where " + query;
		
		try
		{
			Map<String, IdTrade> tradeIds = new HashMap<>();
			IdTrade idTrade = new IdTrade("TradeType","IdTrade",1);
			tradeIds.put(idTrade.getId(), idTrade);

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
	
	public Map<String, Trade> getTrades(Map<String, IdTrade> tradeIds) throws IOException, JsonProcessingException{
		
		/// the database context is IdTrade, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT TradeXML from SummitTradeData where TradeType = ? and IdTrade = ? and TradeVersion = ?";
		
		try
		{
			Map<String, Trade> trades = new HashMap<>();

			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);

			/// TBDAA - execute this in batchs of 1000
			for (Map.Entry<String, IdTrade> tradeIdEntry: tradeIds.entrySet() ) {

				IdTrade idTrade = tradeIdEntry.getValue();
    			StringBuffer outXMLResponse = new StringBuffer();
    			Vector<StringBuffer> messageList = new Vector<StringBuffer>();

                String tradeSQL = "SELECT TradeJSON from SummitTradeData where TradeType = " + idTrade.getTradeType() +
                                                    " and IdTrade = " + idTrade.getTradeId() +
                                                    " and TradeVersion = " + idTrade.getTradeVersion();

				JsonNode tradeJson = jsonMapper.readTree("TradeJSON");
				Trade trade = new Trade(idTrade, tradeJson);
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
