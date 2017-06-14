package com.gms.datasource;

/**
 * Created by gms on 6/14/2017.
 */
public class MktIdFXRate extends MktId {
    String ccy1;
    String ccy2;

    public MktIdFXRate(String ccy1, String ccy2){
        setClassId("MktIdFXRate");
        this.ccy1 = ccy1;
        this.ccy2 = ccy2;
    }
    public String getRequest(String curveId, String asOfDate){
        return "FXRATE/" + ccy1 + "/" + ccy2;
    }

    public String getId(String curveId, String asOf){
        return "FXRate_" + curveId + "_" + asOf + "_" + getCcy1() + "_" + getCcy2();
    }


    public String getCcy1() {
        return ccy1;
    }

    public void setCcy1(String ccy1) {
        this.ccy1 = ccy1;
    }

    public String getCcy2() {
        return ccy2;
    }

    public void setCcy2(String ccy2) {
        this.ccy2 = ccy2;
    }

}
