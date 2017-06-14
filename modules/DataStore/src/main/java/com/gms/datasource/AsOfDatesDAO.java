package com.gms.datasource;

import summit.etkapi_ws.SU_eToolkitAPIException;

import java.util.List;
import java.util.Map;

/**
 * Created by gms on 5/27/2017.
 */
public interface AsOfDatesDAO {
    List<String> getValues() throws SU_eToolkitAPIException, InterruptedException;

}
