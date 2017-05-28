package com.gms.RESTApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.Trade;
import com.gms.datasource.TradeId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gms.datasource.TradesDAO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class RESTTester {
    public static void main(String[] args){
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        if (appContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext)appContext).registerShutdownHook();
        }

        TradesDAO trades = appContext.getBean("tradesDAO",TradesDAO.class);
        try {
            List<TradeId> tradeIdList = trades.getTradeIds(" and dmOwnerTable in ('SWAP') and TradeId in ('11301C')");
            ObjectMapper mapper = new ObjectMapper();
            String tradeIdsJson = mapper.writeValueAsString(tradeIdList);
            List<Trade> tradesList = trades.getTrades(tradeIdsJson);

        }catch (IOException e){
            e.printStackTrace();
        }

        ThreadPoolTaskExecutor taskExecutor = appContext.getBean("taskExecutor",ThreadPoolTaskExecutor.class);
        taskExecutor.shutdown();


        System.out.println("First Running application.");
        System.out.println(trades.getClass());
    }

}
