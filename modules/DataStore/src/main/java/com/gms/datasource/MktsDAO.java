package com.gms.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by gms on 5/30/2017.
 */
public interface MktsDAO {
    public List<MktId> getMktIds(List<Trade> trades) throws IOException, JsonProcessingException;
    public List<Mkt> getMkts(List<MktId> mktIds, String curveId, String asOfDate) throws IOException, JsonProcessingException;
}
