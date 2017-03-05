package com.gms.datasource;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface TradesDAO {
	public String getTradeIds(String query) throws IOException, JsonProcessingException;
	public String getTrades(String jsonIds) throws IOException, JsonProcessingException;
}