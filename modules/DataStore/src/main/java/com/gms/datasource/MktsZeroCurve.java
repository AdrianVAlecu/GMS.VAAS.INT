package com.gms.datasource;

import java.util.Map;

/**
 * Created by gms on 6/3/2017.
 */
public class MktsZeroCurve extends Mkts {
    private IdMktIR mktId;
    private Map<String, String> points;

    public MktsZeroCurve(IdMktIR mktId, Map<String, String> points){
        this.mktId = mktId;
        this.points = points;
    }

    public IdMktIR getMktId() {
        return mktId;
    }

    public void setMktId(IdMktIR mktId) {
        this.mktId = mktId;
    }
}
