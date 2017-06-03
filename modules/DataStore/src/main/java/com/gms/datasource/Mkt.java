package com.gms.datasource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by gms on 5/30/2017.
 */
public class Mkt {
    public MktId mktId;
    public List<JsonNode> mkt;
}
