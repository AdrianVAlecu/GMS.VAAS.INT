package com.gms.datasource;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by gms on 7/17/2017.
 */
public class IdTradesConverter{

    public List<IdTrade> convert(Map<String,IdTrade> map) {
        List<IdTrade> list = new LinkedList<>();
        for ( Map.Entry<String, IdTrade> it : map.entrySet() ){
            list.add(it.getValue());
        }
        return list;
    }
}