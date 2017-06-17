package com.gms.datasource;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by gms on 5/30/2017.
 */
@JsonTypeName("IdMktIR")
public class IdMktIR extends IdMkt {

    String ccy;
    String index;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getId(String curveId, String asOf){
        return "IR_" + curveId + "_" + asOf + "_" + getCcy() + "_" + getIndex();
    }

    public IdMktIR(String ccy, String index){
        setClassId("IdMktIR");
        this.ccy = ccy;
        this.index = index;
    }
    public String getRequest(String curveId, String asOfDate){
        return "<Request><Ccy>" + ccy + "</Ccy><Index>" + index + "</Index><CurveId>" +
                curveId + "</CurveId><AsOfDate>" + asOfDate + "</AsOfDate></Request>";
    }
}
