package com.gms.datasource;

import java.util.Map;

/**
 * Created by gms on 6/3/2017.
 */
public class MktZeroCurve extends Mkt {
    private MktIdIR mktId;
    private Map<String, String> points;

    public MktZeroCurve(MktIdIR mktId, Map<String, String> points){
        this.mktId = mktId;
        this.points = points;
    }

    public MktIdIR getMktId() {
        return mktId;
    }

    public void setMktId(MktIdIR mktId) {
        this.mktId = mktId;
    }
}
