package com.gms.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gms on 5/30/2017.
 */
public interface DAOMkts {
    public Map<String, IdMkt> getMktIds(Map<String, Trade> trades) throws IOException, JsonProcessingException;
    public Map<String, Mkts> getMkts(Map<String, IdMkt> mktIds, String curveId, String asOfDate) throws IOException, JsonProcessingException;
}
