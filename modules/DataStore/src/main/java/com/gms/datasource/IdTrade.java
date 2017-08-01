package com.gms.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author GMS
 *
 */
public class IdTrade {
	String Id;
	String TradeType;
	String TradeId;
	int TradeVersion;

	public IdTrade(String Id, String TradeType, String TradeId, int TradeVersion)
	{
		this.TradeType = TradeType;
		this.TradeId = TradeId;
		this.TradeVersion = TradeVersion;
		this.Id = "TRADE_" + getTradeType() + "_" + getTradeId() + "_" + getTradeVersion();
	}

	public IdTrade()
	{
		TradeType = "";
		TradeId = "";
		TradeVersion = 0;
		Id = "TRADE_" + getTradeType() + "_" + getTradeId() + "_" + getTradeVersion();
	}
	public IdTrade(String tradeType, String tradeId, int tradeVersion)
	{
		TradeType = tradeType;
		TradeId = tradeId;
		TradeVersion = tradeVersion;
		Id = "TRADE_" + getTradeType() + "_" + getTradeId() + "_" + getTradeVersion();
	}

	public void setId(String id) {
		Id = id;
	}

	public String getId(){
		return Id;
	}
	/**
	 * @return the idTrade
	 */
	public String getTradeId() {
		return TradeId;
	}

	/**
	 * @return the tradeType
	 */
	public String getTradeType() {
		return TradeType;
	}

	/**
	 * @return the tradeVersion
	 */
	public int getTradeVersion() {
		return TradeVersion;
	}

	/**
	 * @param tradeId
	 *            the idTrade to set
	 */
	public void setTradeId(String tradeId) {
		TradeId = tradeId;
	}

	/**
	 * @param tradeType
	 *            the tradeType to set
	 */
	public void setTradeType(String tradeType) {
		TradeType = tradeType;
	}

	/**
	 * @param tradeVersion
	 *            the tradeVersion to set
	 */
	public void setTradeVersion(int tradeVersion) {
		TradeVersion = tradeVersion;
	}
}


