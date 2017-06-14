package com.gms.datasource;

/**
 * Created by gms on 6/14/2017.
 */
public class MktIdFXVol extends MktId {
    String ccy1;
    String ccy2;

    public MktIdFXVol(String ccy1, String ccy2){
        setClassId("MktIdFXRate");
        this.ccy1 = ccy1;
        this.ccy2 = ccy2;
    }
    public String getRequest(String curveId, String asOfDate){
        return "FXVOL/" + ccy1 + "/" + ccy2;
    }

    public String getId(String curveId, String asOf){
        return "FXVol_" + curveId + "_" + asOf + "_" + getCcy1() + "_" + getCcy2();
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
