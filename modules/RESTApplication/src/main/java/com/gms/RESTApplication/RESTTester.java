package com.gms.RESTApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RESTTester {
    public static void main(String[] args){
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        if (appContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext)appContext).registerShutdownHook();
        }

        TradesDAO trades = appContext.getBean("tradesDAO",TradesDAO.class);
        MktsDAO mkts = appContext.getBean("mktsDAO", MktsDAO.class);
        AsOfDatesDAO asOfDates = appContext.getBean("asOfDates", AsOfDatesDAO.class);

        try {
            List<TradeId> tradeIdsList = trades.getTradeIds(" and dmOwnerTable in ('SWAP', 'MM', 'MUST_TR') and TradeId in ('11388L1') ");
            ObjectMapper mapper = new ObjectMapper();
            String tradeIdsJson = mapper.writeValueAsString(tradeIdsList);
            List<Trade> tradesList = trades.getTrades(tradeIdsList);

            List<MktId> mktIdsList = mkts.getMktIds(tradesList);
            String curveId = "EOD"; //EOD / EODDVM / RISK / RISKC1Y
            List<String> dates = asOfDates.getValues();
            for (String date : dates ) {
                List<Mkt> mktsList = mkts.getMkts(mktIdsList, curveId, date);
                if ( mktsList.isEmpty() ) {
                    System.out.println("Failed to get market cvId: " + curveId + " asOfdate: " + date);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        ThreadPoolTaskExecutor taskExecutor = appContext.getBean("taskExecutor",ThreadPoolTaskExecutor.class);
        taskExecutor.shutdown();


        System.out.println("First Running application.");
        System.out.println(trades.getClass());
    }

}
