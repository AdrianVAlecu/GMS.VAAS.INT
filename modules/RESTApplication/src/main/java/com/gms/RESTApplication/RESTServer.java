package com.gms.RESTApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class RESTServer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RESTServer.class);
    }

    public static void main(String[] args){
        SpringApplication.run(RESTServer.class, args);

        /*
        ApplicationContext appContext = new ClassPathXmlApplicationContext("summit-spring.xml");

        if (appContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext)appContext).registerShutdownHook();
        }

        DAOTrades trades = appContext.getBean("tradesDAO",DAOTrades.class);
        DAOMkts mkts = appContext.getBean("mktsDAO", DAOMkts.class);
        DAOAsOfDates asOfDates = appContext.getBean("asOfDates", DAOAsOfDates.class);

        try {
            List<IdTrade> tradeIdsList = trades.getTradeIds(" and dmOwnerTable in ('SWAP', 'MM', 'MUST_TR') and IdTrade in ('11388L1') ");
            ObjectMapper mapper = new ObjectMapper();
            String tradeIdsJson = mapper.writeValueAsString(tradeIdsList);
            List<Trade> tradesList = trades.getTrades(tradeIdsList);

            List<IdMkt> mktIdsList = mkts.getMktIds(tradesList);
            String curveId = "EOD"; //EOD / EODDVM / RISK / RISKC1Y
            List<String> dates = asOfDates.getValues();
            for (String date : dates ) {
                List<Mkts> mktsList = mkts.getMkts(mktIdsList, curveId, date);
                if ( mktsList.isEmpty() ) {
                    System.out.println("Failed to get market cvId: " + curveId + " asOfdate: " + date);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        ThreadPoolTaskExecutor taskExecutor = appContext.getBean("taskExecutor",ThreadPoolTaskExecutor.class);
        if ( taskExecutor != null ){
            taskExecutor.shutdown();
        }

        */
        System.out.println("First Running application.");
    }

}
