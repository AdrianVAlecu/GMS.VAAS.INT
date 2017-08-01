package com.gms.datasource;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GMS on 05/03/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:summit-spring.xml")
@ImportResource({"classpath:Stylesheet_EntList.xsl", "classpath:Stylesheet_Identity.xsl"})
public class DAOTradesTest {

    @Resource
    private DAOTrades DAOTrades;

    @Test
    public void getTradeIds() {
        try {
            IdTrades tradeIds = DAOTrades.getTradeIds("");
            Assert.assertNotNull(tradeIds);
            Assert.assertTrue(tradeIds.getIdTrades().size() > 0 );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getTrades() {
        try {
            Map<String, IdTrade> tradeIds = new HashMap<>();
            IdTrade idTrade = new IdTrade("MM","11312C", 1);
            tradeIds.put(idTrade.getId(), idTrade);
            Map<String, Trade> trades = DAOTrades.getTrades(tradeIds);
            Assert.assertNotNull(trades);
            Assert.assertTrue(trades.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
