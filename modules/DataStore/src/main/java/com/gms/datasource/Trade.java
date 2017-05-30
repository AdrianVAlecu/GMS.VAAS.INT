package com.gms.datasource;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author GMS
 *
 */
public class Trade {
	TradeId tradeId;
	JsonNode tradeJSON;

	public Trade(TradeId tradeId, JsonNode tradeJSON)
	{
		this.tradeId = tradeId;
		this.tradeJSON = tradeJSON;
	}
	/**
	 * @return the trade
	 */
	public JsonNode getTradeXML() {
		return this.tradeJSON;
	}

	/**
	 * @return the tradeId
	 */
	public TradeId getTradeId() {
		return this.tradeId;
	}

	/**
	 * @param tradeJSON
	 *            the trade to set
	 */
	public void setTradeXML(JsonNode tradeJSON) {
		this.tradeJSON = tradeJSON;
	}

	/**
	 * @param tradeId
	 *            the tradeId to set
	 */
	public void setTradeId(TradeId tradeId) {
		this.tradeId = tradeId;
	}
}

