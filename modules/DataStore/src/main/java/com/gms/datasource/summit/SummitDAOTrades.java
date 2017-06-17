package com.gms.datasource.summit;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.gms.datasource.IdTrade;
import summit.etkapi_ws.SU_eToolkitAPIException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.Trade;
import com.gms.datasource.DAOTrades;

public class SummitDAOTrades implements DAOTrades {
    
	private SWrapEToolKit etkWrap;
	private SWrapXSLT sXslt;
	private SWrapJSON sJson;
	private SWrapFile sFile;

	public SummitDAOTrades(SWrapEToolKit etkWrap, String documentPath) {
        this.etkWrap = etkWrap;

        sXslt = new SWrapXSLT();
        sJson = new SWrapJSON();
        sFile = new SWrapFile(documentPath);
    }

	public Map<String, IdTrade> getTradeIds(String query) throws IOException, JsonProcessingException{
		
		/// the database context is IdTrade, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		/// String sql = "SELECT TradeType, IdTrade, TradeVersion from SummitTradeData where " + query;
		String sql = "SELECT IdTrade, dmOwnerTable, Audit_Version from dmENV where Audit_Current = 'Y' " + query;
		
		try
		{
			Map<String, IdTrade> tradeIds = new HashMap<>();

			List<List<String>> queryResult = etkWrap.executeDBQuery(sql);

			for ( List<String> row : queryResult ){
				IdTrade idTrade = new IdTrade(row.get(1),row.get(0),Integer.parseInt(row.get(2)));
				tradeIds.put(idTrade.getId(), idTrade);
			}
			ObjectMapper mapper = new ObjectMapper();
			return tradeIds;
		}
		catch (SU_eToolkitAPIException | InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		finally 
		{
		}
	}
	
	public Map<String, Trade> getTrades(Map<String, IdTrade> tradeIds){

		try
		{
			Map<String, Trade> trades = new HashMap<>();

			Map<String, String> entities = new HashMap<>();

			for (Map.Entry<String, IdTrade> entry: tradeIds.entrySet() ) {
				IdTrade idTrade = entry.getValue();
				String entity = etkWrap.executeEntityCreate(idTrade.getTradeType());
				entity = entity.replace("<IdTrade/>", "<IdTrade>" + idTrade.getTradeId() + "</IdTrade>");
				entities.put(idTrade.getId(), entity);
			}
			List<SWrapEToolKit.ETKResponse> tradesStr = etkWrap.execute("s_base::EntityRead", entities);

			for (SWrapEToolKit.ETKResponse tradeResp : tradesStr) {
				IdTrade idTrade = tradeIds.get(tradeResp.getId());
				JsonNode tradeNode = sJson.readXML(sXslt.applyEntList(tradeResp.getResponse()));
				trades.put(idTrade.getId(), new Trade(idTrade, tradeNode));

				sFile.writeStringToFile(idTrade.getTradeType() + "_" +
							idTrade.getTradeId() + "_" +
							idTrade.getTradeVersion() + ".json",
						sJson.writeJSON(tradeNode));
			}

			return trades;
		}catch (SU_eToolkitAPIException | InterruptedException e){
			throw new RuntimeException(e);
		}
		finally
		{
		}

	}
}
