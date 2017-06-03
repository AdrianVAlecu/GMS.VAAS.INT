package com.gms.datasource;

import java.util.Map;

/**
 * Created by gms on 6/3/2017.
 */
public class IRMkt extends Mkt {
    private IRMktId mktId;
    private Map<String, String> points;

    public IRMkt(IRMktId mktId, Map<String, String> points){
        this.mktId = mktId;
        this.points = points;
    }

    public IRMktId getMktId() {
        return mktId;
    }

    public void setMktId(IRMktId mktId) {
        this.mktId = mktId;
    }
}
