package com.gms.datasource.MySQL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.IdTrade;
import com.gms.datasource.Trade;
import com.gms.datasource.DAOTrades;

public class MySQLDAOTrades implements DAOTrades {
	
	private DataSource dataSource;
	
	public Map<String, IdTrade> getTradeIds(String query) throws IOException, JsonProcessingException{
		
		/// the database context is IdTrade, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT TradeType, IdTrade, TradeVersion from SummitTradeData where ?";
		
		Connection conn = null;
		
		try
		{
			Map<String, IdTrade> tradeIds = new HashMap<>();
			
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,query);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				IdTrade idTrade = new IdTrade(rs.getString("TradeType"),rs.getString("IdTrade"),rs.getInt("TradeVersion"));
				tradeIds.put(idTrade.getId(), idTrade);
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
	
	public Map<String, Trade> getTrades(Map<String, IdTrade> tradeIds) throws IOException, JsonProcessingException{
		
		/// the database context is IdTrade, TradeType, TradeVersion, other index columns that can be used in the query ... , TradeXML or TradeJSON
		String sql = "SELECT tradeJSON from SummitTradeData where TradeType = ? and IdTrade = ? and TradeVersion = ?";
		
		Connection conn = null;
		
		try
		{
			Map<String, Trade> trades = new HashMap<>();
			
			/// TBDAA - execute this in batchs of 1000
			for (Map.Entry<String, IdTrade> tradeIdEntry: tradeIds.entrySet() ) {

				IdTrade idTrade = tradeIdEntry.getValue();
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, idTrade.getTradeType());
				ps.setString(2, idTrade.getTradeId());
				ps.setInt(3, idTrade.getTradeVersion());
				ResultSet rs = ps.executeQuery();
				ObjectMapper jsonMapper = new ObjectMapper();
				jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);

				while(rs.next())
				{
					JsonNode tradeJson = jsonMapper.readTree(rs.getString("tradeJSON"));
					Trade trade = new Trade(idTrade, tradeJson);
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

