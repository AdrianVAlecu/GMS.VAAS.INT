package com.gms.datasource.summit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gms.datasource.IdTrades;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gms on 6/3/2017.
 */
public class SWrapJSON {
    private ObjectMapper jsonMapper;
    private XmlMapper xmlMapper;

    public SWrapJSON(){
        jsonMapper = new ObjectMapper();
        //jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        //jsonMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
        xmlMapper = new XmlMapper();
    }

    public IdTrades convertIdTrades(String json){
        try {
            return jsonMapper.readValue(json, IdTrades.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode readXML(String xml){
        try {
            return xmlMapper.readTree(xml.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String writeJSON(JsonNode node){
        try {
            return xmlMapper.writeValueAsString(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String writeXML(Object o){
        try {
            return xmlMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
    public String writeJSON(Object o){
        try {
            return jsonMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
