package com.gms.datasource;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author GMS
 *
 */
public class Trade {
	IdTrade idTrade;
	JsonNode tradeJSON;

	public Trade(IdTrade idTrade, JsonNode tradeJSON)
	{
		this.idTrade = idTrade;
		this.tradeJSON = tradeJSON;
	}
	/**
	 * @return the trade
	 */
	public JsonNode getTradeJSON() {
		return this.tradeJSON;
	}

	/**
	 * @return the idTrade
	 */
	public IdTrade getIdTrade() {
		return this.idTrade;
	}

	/**
	 * @param tradeJSON
	 *            the trade to set
	 */
	public void setTradeJSON(JsonNode tradeJSON) {
		this.tradeJSON = tradeJSON;
	}

	/**
	 * @param idTrade
	 *            the idTrade to set
	 */
	public void setIdTrade(IdTrade idTrade) {
		this.idTrade = idTrade;
	}
}

