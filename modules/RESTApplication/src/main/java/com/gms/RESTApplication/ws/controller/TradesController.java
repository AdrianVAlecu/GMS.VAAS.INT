package com.gms.RESTApplication.ws.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;
import com.gms.datasource.TradesDAO;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by GMS on 05/03/2017.
 */
@RestController
@RequestMapping(value="/api")
@ContextConfiguration({"classpath:applicationContext.xml"})
public class TradesController {

    @Resource
    private TradesDAO tradesDAO;

    @RequestMapping(value="/trade/{id}", method= RequestMethod.GET)
    public List<TradeId> getTrade(@PathVariable("id") String query) throws IOException, JsonProcessingException {
        return tradesDAO.getTradeIds("and TradeId in ('" + query + "')");
    }

    @RequestMapping(value="/trades", method=RequestMethod.GET)
    public List<Trade> getTrades(@PathVariable List<TradeId> tradeIds) throws IOException, JsonProcessingException {
        return tradesDAO.getTrades(tradeIds);
    }
}
