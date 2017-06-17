package com.gms.datasource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DAOTrades {

	public Map<String, TradeId> getTradeIds(String query) throws IOException, JsonProcessingException;
	public Map<String, Trade> getTrades(Map<String, TradeId> tradeIds) throws IOException, JsonProcessingException;
}