package com.gms.datasource;

/**
 * @author GMS
 *
 */
public class Trade {
	TradeId tradeId;
	String tradeJSON;

	public Trade(TradeId tradeId, String tradeJSON)
	{
		this.tradeId = tradeId;
		this.tradeJSON = tradeJSON;
	}
	/**
	 * @return the trade
	 */
	public String getTradeXML() {
		return this.tradeJSON;
	}

	/**
	 * @return the tradeId
	 */
	public TradeId getTradeId() {
		return this.tradeId;
	}

	/**
	 * @param trade
	 *            the trade to set
	 */
	public void setTradeXML(String tradeJSON) {
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

