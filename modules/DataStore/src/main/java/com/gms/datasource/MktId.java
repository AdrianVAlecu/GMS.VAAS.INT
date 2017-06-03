package com.gms.datasource;

import com.fasterxml.jackson.annotation.*;

/**
 * Created by gms on 5/30/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IRMktId.class, name = "IRMktId")
})
public abstract class MktId {

    private String classId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public abstract String getRequest(String curveId, String asOfDate);
}
