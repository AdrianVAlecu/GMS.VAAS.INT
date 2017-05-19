package com.gms.datasource;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface TradesDAO {

	public List<TradeId> getTradeIds(String query) throws IOException, JsonProcessingException;
	public List<Trade> getTrades(String jsonIds) throws IOException, JsonProcessingException;
}