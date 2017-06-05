package com.gms.datasource;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMS on 05/03/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TradesDAOTest {

    @Resource
    private TradesDAO tradesDAO;

    @Test
    public void getTradeIds() {
        try {
            List<TradeId> tradeIds = tradesDAO.getTradeIds("");
            Assert.assertNotNull(tradeIds);
            Assert.assertTrue(tradeIds.size() > 0 );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getTrades() {
        try {
            List<TradeId> tradeIds = new ArrayList<>();
            tradeIds.add(new TradeId("MM","11312C", 1));
            List<Trade> trades = tradesDAO.getTrades(tradeIds);
            Assert.assertNotNull(trades);
            Assert.assertTrue(trades.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
