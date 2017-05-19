package com.gms.datasource;

/**
 * @author GMS
 *
 */
public class TradeId {
	String TradeType;
	String TradeId;
	int TradeVersion;

	public TradeId()
	{
		TradeType = "";
		TradeId = "";
		TradeVersion = 0;
	}
	public TradeId(String tradeType, String tradeId, int tradeVersion)
	{
		TradeType = tradeType;
		TradeId = tradeId;
		TradeVersion = tradeVersion;
	}
	
	/**
	 * @return the tradeId
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
	 *            the tradeId to set
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


