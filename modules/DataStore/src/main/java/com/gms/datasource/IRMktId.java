package com.gms.datasource;

/**
 * Created by gms on 5/30/2017.
 */
public class IRMktId implements MktId {
    String ccy;
    String index;

    public String getRequest(String curveId, String asOfDate){
        return "<Request><Ccy>" + ccy + "</Ccy><Index>" + index + "</Index><CurveId>" +
                curveId + "</CurveId><AsOfDate>" + asOfDate + "</AsOfDate></Request>";
    }
}
