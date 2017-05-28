package com.gms.datasource.summit;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.FileUtils;
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
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class SummitTradesDAO implements TradesDAO {
    
	private EToolKitWrapper etkWrap;
	private String documentPath;

	public SummitTradesDAO(EToolKitWrapper etkWrap, String documentPath) {
        this.etkWrap = etkWrap;
        this.documentPath = documentPath;
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
	
	public List<Trade> getTrades(String jsonTradeIds) throws IOException, JsonProcessingException{

		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON

		try
		{
			ObjectMapper mapper = new ObjectMapper();

			List<TradeId> tradeIds = mapper.readValue(jsonTradeIds, new TypeReference<List<TradeId>>(){});
			List<Trade> trades = new Vector<Trade>();

			Vector<String> entities = new Vector<>();

			for (TradeId tradeId: tradeIds ) {
				String entity = etkWrap.executeEntityCreate(tradeId.getTradeType());
				entity = entity.replace("<TradeId/>", "<TradeId>" + tradeId.getTradeId() + "</TradeId>");
				entities.add(entity);
			}
			Vector<String> tradesStr = etkWrap.execute("s_base::EntityRead", entities);
			int index = 0;
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);

			for (TradeId tradeId : tradeIds) {
				String tradeStr = tradesStr.get(index);
				index++;

				Source tradeSource = new StreamSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>" + tradeStr));

				StreamSource xslt = new StreamSource(tradeId.getTradeType()+"_Stylesheet.xsl");
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = transFactory.newTransformer(xslt);
				StringWriter resultWriter = new StringWriter();
				StreamResult result = new StreamResult(resultWriter);
				transformer.transform(tradeSource, result);
				String tradeStrTransf = resultWriter.toString();


				XmlMapper xmlMapper = new XmlMapper();
				JsonNode node = xmlMapper.readTree(tradeStrTransf.getBytes());
				String jsonTrade = jsonMapper.writeValueAsString(node);

				String fileName = documentPath + "/" + tradeId.getTradeType() + "_" + tradeId.getTradeId() + "_" + tradeId.getTradeVersion() + ".json";
				File jsonFile = new File(fileName);
				System.out.println("Trying to write file to disk: " + jsonFile.getCanonicalPath());
				FileUtils.writeStringToFile(new File(fileName), jsonTrade);
			}

			return trades;
		}
		catch (SU_eToolkitAPIException | InterruptedException e){
			throw new RuntimeException(e);
		}catch(IOException | TransformerException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally
		{
		}

	}
}
