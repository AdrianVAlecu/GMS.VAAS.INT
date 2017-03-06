package com.gms.datasource.summit;

import java.util.List;
import java.util.Vector;
import java.io.IOException;

import summit.etkapi_ws.SU_eToolkitAPI;
import summit.etkapi_ws.SU_eToolkitAPIException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;
import com.gms.datasource.TradesDAO;

public class SummitTradesDAO implements TradesDAO {
    
	private SU_eToolkitAPI etkAPI;

	public SummitTradesDAO(SU_eToolkitAPI etkAPI, String user, String pass, String application, String type, String dbEnv, String extraParams)  throws SU_eToolkitAPIException, Exception {
        System.out.println("We are using constructor injector injection for SU_eToolkitAPI, user: " + user +
                " pass: " + pass + " context: " + application + " type: " + type + " dbEnv: " + dbEnv + " extraParams: " + extraParams );
        this.etkAPI = etkAPI;
        try
		{
        	this.etkAPI.Connect(user, pass, application, type, dbEnv, extraParams);
		}
		catch(SU_eToolkitAPIException e)
		{
			System.out.print(e.GetMsg());
		}

    }

	public List<TradeId> getTradeIds(String query) throws IOException, JsonProcessingException{
		
		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		/// String sql = "SELECT TradeType, TradeId, TradeVersion from SummitTradeData where " + query;
		String sql = "SELECT Id, Audit_Version, Audit_Action from dmCUSTOMER where 1=1 " + query;
		
		try
		{
			List<TradeId> tradeIds = new Vector<TradeId>();
			StringBuffer outXMLResponse = new StringBuffer();
			Vector<String> messageList = new Vector<String>();
			
			etkAPI.Execute("s_base::DBQuery","<Request><SummitSQL>"+sql+"</SummitSQL></Request>",outXMLResponse, messageList);
			//while(rs.next())
			{
				TradeId tradeId = new TradeId("TradeType","TradeId",1);
				tradeIds.add(tradeId);
			}
			ObjectMapper mapper = new ObjectMapper();
			return tradeIds;
		}
		catch (SU_eToolkitAPIException e)
		{
			throw new RuntimeException(e);
		}
		finally 
		{
		}
	}
	
	public List<Trade> getTrades(String jsonTradeIds) throws IOException, JsonProcessingException{

		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON

		try
		{
			ObjectMapper mapper = new ObjectMapper();

			List<TradeId> tradeIds = mapper.readValue(jsonTradeIds, new TypeReference<List<TradeId>>(){});
			List<Trade> trades = new Vector<Trade>();

			/// TBDAA - execute this in batchs of 1000
			for (TradeId tradeId: tradeIds ) {

    			StringBuffer outXMLResponse = new StringBuffer();
    			Vector<String> messageList = new Vector<String>();

                String tradeSQL = "SELECT TradeJSON from SummitTradeData where TradeType = " + tradeId.getTradeType() +
                                                    " and TradeId = " + tradeId.getTradeId() +
                                                    " and TradeVersion = " + tradeId.getTradeVersion();
			    etkAPI.Execute("s_base::DBQuery","<Request><SummitSQL>"+tradeSQL+"</SummitSQL></Request>",outXMLResponse, messageList);

				//while(rs.next())
				{
					Trade trade = new Trade(tradeId, "TradeJSON");
					trades.add(trade);
				}
			}

			return trades;
		}
		catch (SU_eToolkitAPIException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
		}

	}
}
