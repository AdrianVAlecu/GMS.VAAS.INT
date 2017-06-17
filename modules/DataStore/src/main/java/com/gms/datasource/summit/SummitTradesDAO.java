package com.gms.datasource.summit;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import summit.etkapi_ws.SU_eToolkitAPIException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;
import com.gms.datasource.TradesDAO;

public class SummitTradesDAO implements TradesDAO {
    
	private SWrapEToolKit etkWrap;
	private SWrapXSLT sXslt;
	private SWrapJSON sJson;
	private SWrapFile sFile;

	public SummitTradesDAO(SWrapEToolKit etkWrap, String documentPath) {
        this.etkWrap = etkWrap;

        sXslt = new SWrapXSLT();
        sJson = new SWrapJSON();
        sFile = new SWrapFile(documentPath);
    }

	public Map<String, TradeId> getTradeIds(String query) throws IOException, JsonProcessingException{
		
		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		/// String sql = "SELECT TradeType, TradeId, TradeVersion from SummitTradeData where " + query;
		String sql = "SELECT TradeId, dmOwnerTable, Audit_Version from dmENV where Audit_Current = 'Y' " + query;
		
		try
		{
			Map<String, TradeId> tradeIds = new HashMap<>();

			List<List<String>> queryResult = etkWrap.executeDBQuery(sql);

			for ( List<String> row : queryResult ){
				TradeId tradeId = new TradeId(row.get(1),row.get(0),Integer.parseInt(row.get(2)));
				tradeIds.put(tradeId.getId(), tradeId);
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
	
	public Map<String, Trade> getTrades(Map<String, TradeId> tradeIds){

		try
		{
			Map<String, Trade> trades = new HashMap<>();

			Map<String, String> entities = new HashMap<>();

			for (Map.Entry<String, TradeId> entry: tradeIds.entrySet() ) {
				TradeId tradeId = entry.getValue();
				String entity = etkWrap.executeEntityCreate(tradeId.getTradeType());
				entity = entity.replace("<TradeId/>", "<TradeId>" + tradeId.getTradeId() + "</TradeId>");
				entities.put(tradeId.getId(), entity);
			}
			List<SWrapEToolKit.ETKResponse> tradesStr = etkWrap.execute("s_base::EntityRead", entities);

			for (SWrapEToolKit.ETKResponse tradeResp : tradesStr) {
				TradeId tradeId = tradeIds.get(tradeResp.getId());
				JsonNode tradeNode = sJson.readXML(sXslt.applyEntList(tradeResp.getResponse()));
				trades.put(tradeId.getId(), new Trade(tradeId, tradeNode));

				sFile.writeStringToFile(tradeId.getTradeType() + "_" +
							tradeId.getTradeId() + "_" +
							tradeId.getTradeVersion() + ".json",
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
