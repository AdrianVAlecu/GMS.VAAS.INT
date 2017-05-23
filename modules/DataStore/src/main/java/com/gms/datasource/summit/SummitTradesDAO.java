package com.gms.datasource.summit;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import summit.etkapi_ws.SU_eToolkitAPI;
import summit.etkapi_ws.SU_eToolkitAPIException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;
import com.gms.datasource.TradesDAO;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SummitTradesDAO implements TradesDAO {
    
	private EToolKitWrapper etkWrap;

	public SummitTradesDAO(EToolKitWrapper etkWrap) {
        this.etkWrap = etkWrap;
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

				etkWrap.executeEntityCreate(tradeId.getTradeType());
    			StringBuffer outXMLResponse = new StringBuffer();
    			Vector<String> messageList = new Vector<String>();

                String tradeSQL = "SELECT TradeJSON from SummitTradeData where TradeType = " + tradeId.getTradeType() +
                                                    " and TradeId = " + tradeId.getTradeId() +
                                                    " and TradeVersion = " + tradeId.getTradeVersion();
			    etkWrap.executeDBQuery("<Request><SummitSQL>"+tradeSQL+"</SummitSQL></Request>");

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
