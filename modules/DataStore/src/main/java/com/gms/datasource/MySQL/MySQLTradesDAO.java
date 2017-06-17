package com.gms.datasource.MySQL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;
import com.gms.datasource.TradesDAO;

public class MySQLTradesDAO implements TradesDAO{
	
	private DataSource dataSource;
	
	public Map<String, TradeId> getTradeIds(String query) throws IOException, JsonProcessingException{
		
		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT TradeType, TradeId, TradeVersion from SummitTradeData where ?";
		
		Connection conn = null;
		
		try
		{
			Map<String, TradeId> tradeIds = new HashMap<>();
			
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,query);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				TradeId tradeId = new TradeId(rs.getString("TradeType"),rs.getString("TradeId"),rs.getInt("TradeVersion"));
				tradeIds.put(tradeId.getId(), tradeId);
			}
			rs.close();
			ps.close();
		
			ObjectMapper mapper = new ObjectMapper();
			return tradeIds;
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally 
		{
			if ( conn != null ){
				try {
					conn.close();
				}catch(SQLException e){}
			}
		}
	}
	
	public Map<String, Trade> getTrades(Map<String, TradeId> tradeIds) throws IOException, JsonProcessingException{
		
		/// the database context is TradeId, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT tradeJSON from SummitTradeData where TradeType = ? and TradeId = ? and TradeVersion = ?";
		
		Connection conn = null;
		
		try
		{
			Map<String, Trade> trades = new HashMap<>();
			
			/// TBDAA - execute this in batchs of 1000
			for (Map.Entry<String, TradeId> tradeIdEntry: tradeIds.entrySet() ) {

				TradeId tradeId = tradeIdEntry.getValue();
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1,tradeId.getTradeType());
				ps.setString(2,tradeId.getTradeId());
				ps.setInt(3,tradeId.getTradeVersion());
				ResultSet rs = ps.executeQuery();
				ObjectMapper jsonMapper = new ObjectMapper();
				jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);

				while(rs.next())
				{
					JsonNode tradeJson = jsonMapper.readTree(rs.getString("tradeJSON"));
					Trade trade = new Trade(tradeId, tradeJson);
					trades.put(tradeIdEntry.getKey(), trade);
				}
				rs.close();
				ps.close();
			
			}
			
			return trades;
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally 
		{
			if ( conn != null ){
				try {
					conn.close();
				}catch(SQLException e){}
			}
		}

	}

}

