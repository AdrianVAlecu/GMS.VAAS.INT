package com.gms.datasource;

/**
 * @author GMS
 *
 */
public class IdTrade {
	String TradeType;
	String TradeId;
	int TradeVersion;

	public IdTrade()
	{
		TradeType = "";
		TradeId = "";
		TradeVersion = 0;
	}
	public IdTrade(String tradeType, String tradeId, int tradeVersion)
	{
		TradeType = tradeType;
		TradeId = tradeId;
		TradeVersion = tradeVersion;
	}

	public String getId(){
		return "TRADE_" + getTradeType() + "_" + getTradeId() + "_" + getTradeVersion();
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


