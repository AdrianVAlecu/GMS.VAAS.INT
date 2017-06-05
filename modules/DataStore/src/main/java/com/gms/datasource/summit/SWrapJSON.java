package com.gms.datasource.summit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

/**
 * Created by gms on 6/3/2017.
 */
public class SWrapJSON {
    private ObjectMapper jsonMapper;
    XmlMapper xmlMapper;

    public SWrapJSON(){
        jsonMapper = new ObjectMapper();
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper = new XmlMapper();
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

    public String writeJSON(Object o){
        try {
            return xmlMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}