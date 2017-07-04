package com.gms.RESTApplication.ws.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.IdTrade;
import com.gms.datasource.Trade;
import com.gms.datasource.DAOTrades;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
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
@RequestMapping(value="/")
@ImportResource({"classpath:summit-spring.xml"})
@PropertySource(value={"classpath:summit.app.properties"})
public class ControllerEtk {
    final static Logger log = Logger.getLogger(ControllerEtk.class);

    @Resource
    private DAOTrades DAOTrades;

    @RequestMapping(value="/trade/{id}", method= RequestMethod.GET)
    public Map<String, IdTrade> getTrade(@PathVariable("id") String query) throws IOException, JsonProcessingException {
        return DAOTrades.getTradeIds("and TradeId in ('" + query + "')");
    }

    @RequestMapping(value="/trades", method=RequestMethod.GET)
    public Map<String, Trade> getTrades(@PathVariable Map<String, IdTrade> tradeIds) throws IOException, JsonProcessingException {
        return DAOTrades.getTrades(tradeIds);
    }

}
