package com.gms.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;
import com.gms.datasource.TradesDAO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.ws.rs.core.Context;
import java.io.IOException;
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
            List<Trade> trades = tradesDAO.getTrades("");
            Assert.assertNotNull(trades);
            Assert.assertTrue(trades.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
