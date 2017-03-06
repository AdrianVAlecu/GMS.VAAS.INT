package com.gms.datasource.files;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;
import com.gms.datasource.TradesDAO;

public class FilesTradesDAO implements TradesDAO {
    	public List<TradeId> getTradeIds(String query) throws IOException, JsonProcessingException{
		
		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT TradeType, TradeId, TradeVersion from SummitTradeData where " + query;
		
		try
		{
			List<TradeId> tradeIds = new Vector<TradeId>();
			TradeId tradeId = new TradeId("TradeType","TradeId",1);
			tradeIds.add(tradeId);

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
	
	public List<Trade> getTrades(String jsonTradeIds) throws IOException, JsonProcessingException{
		
		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT TradeXML from SummitTradeData where TradeType = ? and TradeId = ? and TradeVersion = ?";
		
		try
		{
			ObjectMapper mapper = new ObjectMapper();	
			
			List<TradeId> tradeIds = mapper.readValue(jsonTradeIds, new TypeReference<List<TradeId>>(){});
			List<Trade> trades = new Vector<Trade>();
			
			/// TBDAA - execute this in batchs of 1000
			for (TradeId tradeId: tradeIds ) {

    			StringBuffer outXMLResponse = new StringBuffer();
    			Vector<StringBuffer> messageList = new Vector<StringBuffer>();

                String tradeSQL = "SELECT TradeJSON from SummitTradeData where TradeType = " + tradeId.getTradeType() + 
                                                    " and TradeId = " + tradeId.getTradeId() +
                                                    " and TradeVersion = " + tradeId.getTradeVersion();

				//while(rs.next())
				{
					Trade trade = new Trade(tradeId, "TradeJSON");
					trades.add(trade);
				}
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
