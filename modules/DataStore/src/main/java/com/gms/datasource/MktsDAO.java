package com.gms.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gms on 5/30/2017.
 */
public interface MktsDAO {
    public Map<String, MktId> getMktIds(Map<String, Trade> trades) throws IOException, JsonProcessingException;
    public Map<String, Mkt> getMkts(Map<String, MktId> mktIds, String curveId, String asOfDate) throws IOException, JsonProcessingException;
}
