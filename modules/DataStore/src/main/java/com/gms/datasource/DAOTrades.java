package com.gms.datasource;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DAOTrades {

	public Map<String, IdTrade> getTradeIds(String query) throws IOException, JsonProcessingException;
	public Map<String, Trade> getTrades(Map<String, IdTrade> tradeIds) throws IOException, JsonProcessingException;
}