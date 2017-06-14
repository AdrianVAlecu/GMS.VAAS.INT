package com.gms.datasource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by gms on 5/30/2017.
 */
@JsonTypeName("MktIdIR")
public class MktIdIR extends MktId {

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

    public MktIdIR(String ccy, String index){
        setClassId("MktIdIR");
        this.ccy = ccy;
        this.index = index;
    }
    public String getRequest(String curveId, String asOfDate){
        return "<Request><Ccy>" + ccy + "</Ccy><Index>" + index + "</Index><CurveId>" +
                curveId + "</CurveId><AsOfDate>" + asOfDate + "</AsOfDate></Request>";
    }
}
