package com.gms.RESTApplication.ws.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.IdTrade;
import com.gms.datasource.Trade;
import com.gms.datasource.DAOTrades;
import com.gms.datasource.summit.SWrapJSON;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * Created by GMS on 05/03/2017.
 */
@RestController
@RequestMapping(value="/etk/")
@ImportResource({"classpath:summit-spring.xml"})
@PropertySource(value={"classpath:summit.app.properties"})
public class ControllerEtk {
    final static Logger log = Logger.getLogger(ControllerEtk.class);
    final static SWrapJSON sJson = new SWrapJSON();

    @Resource
    private DAOTrades DAOTrades;

    @RequestMapping(value="/tradeIds/{query}", method= RequestMethod.GET)
    public String getTrade(@PathVariable("query") String query) throws IOException, JsonProcessingException {
        query = query.replaceAll("_", " ");
        String response = (sJson).writeJSON(DAOTrades.getTradeIds(query));
        return response;
    }

    @RequestMapping(value="/trades", method=RequestMethod.PUT)
    public Map<String, Trade> getTrades(HttpEntity<String> httpEntity) throws IOException, JsonProcessingException {
        String json = httpEntity.getBody();
        return DAOTrades.getTrades(null);
    }

}
