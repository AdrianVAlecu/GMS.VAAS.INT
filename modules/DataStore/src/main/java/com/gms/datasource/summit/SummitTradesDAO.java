package com.gms.datasource.summit;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.FileUtils;
import summit.etkapi_ws.SU_eToolkitAPIException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;
import com.gms.datasource.TradesDAO;

public class SummitTradesDAO implements TradesDAO {
    
	private SWrapEToolKit etkWrap;
	private String documentPath;
	private SWrapXSLT sXslt;
	private SWrapJSON sJson;

	public SummitTradesDAO(SWrapEToolKit etkWrap, String documentPath) {
        this.etkWrap = etkWrap;
        this.documentPath = documentPath;

        sXslt = new SWrapXSLT();
        sJson = new SWrapJSON();
    }

	public List<TradeId> getTradeIds(String query) throws IOException, JsonProcessingException{
		
		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		/// String sql = "SELECT TradeType, TradeId, TradeVersion from SummitTradeData where " + query;
		String sql = "SELECT TradeId, dmOwnerTable, Audit_Version from dmENV where Audit_Current = 'Y' " + query;
		
		try
		{
			List<TradeId> tradeIds = new Vector<TradeId>();

			List<List<String>> queryResult = etkWrap.executeDBQuery(sql);

			for ( List<String> row : queryResult ){
				TradeId tradeId = new TradeId(row.get(1),row.get(0),Integer.parseInt(row.get(2)));
				tradeIds.add(tradeId);
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
	
	public List<Trade> getTrades(List<TradeId> tradeIds) throws IOException, JsonProcessingException{

		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON

		try
		{
			List<Trade> trades = new Vector<Trade>();

			Vector<String> entities = new Vector<>();

			for (TradeId tradeId: tradeIds ) {
				String entity = etkWrap.executeEntityCreate(tradeId.getTradeType());
				entity = entity.replace("<TradeId/>", "<TradeId>" + tradeId.getTradeId() + "</TradeId>");
				entities.add(entity);
			}
			Vector<String> tradesStr = etkWrap.execute("s_base::EntityRead", entities);
			int index = 0;

			for (TradeId tradeId : tradeIds) {
				String tradeStr = tradesStr.get(index);
				index++;

				JsonNode tradeNode = sJson.readXML(sXslt.applyEntList(tradeStr));
				trades.add(new Trade(tradeId, tradeNode));

				String jsonTrade = sJson.writeJSON(tradeNode);

				String fileName = documentPath + "/" + tradeId.getTradeType() + "_" + tradeId.getTradeId() + "_" + tradeId.getTradeVersion() + ".json";
				File jsonFile = new File(fileName);
				System.out.println("Trying to write file to disk: " + jsonFile.getCanonicalPath());
				FileUtils.writeStringToFile(new File(fileName), jsonTrade);
			}

			return trades;
		}catch (SU_eToolkitAPIException | InterruptedException e){
			throw new RuntimeException(e);
		}catch(IOException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally
		{
		}

	}
}
