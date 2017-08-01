package com.gms.RESTApplication.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.RESTApplication.ws.controller.JobStatus;
import com.gms.datasource.IdTrade;
import com.gms.datasource.IdTrades;
import com.gms.datasource.summit.SWrapJSON;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class RESTWorkflow {
    final static Logger log = Logger.getLogger(RESTWorkflow.class);

    SWrapJSON sJson = new SWrapJSON();
    String serverURL = "http://localhost:8181/";
    int tradePackageSize = 400;
    JobStatus jobStatus;


    public RESTWorkflow(JobStatus jobStatus) {
        this.jobStatus = jobStatus;

        this.jobStatus.setJobInfo("ETK.getTradeIds");
        String tradeIdsStr = getCall("etk/tradeIds/" +
                this.jobStatus.getInputParams().getQuery());

        if ( tradeIdsStr.equals("") ) {
            this.jobStatus.setStatus(JobStatus.JobStatusEnum.EMPTY);
            log.debug("Empty response");
            return;
        }


        IdTrades tradeIds = sJson.convertIdTrades(tradeIdsStr);

        int counter = 0;
        List<List<IdTrade>> subsets = new LinkedList<>();
        List<IdTrade> oneSubset = new LinkedList<>();
        for ( IdTrade tradeId : tradeIds.getIdTrades() ){
            oneSubset.add(tradeId);
            counter ++;

            if ( (counter % tradePackageSize) == 0 ){
                subsets.add(oneSubset);
                oneSubset = new LinkedList<>();
            }
        }
        subsets.add(oneSubset);
        this.jobStatus.setJobInfo( "ETK.getTrades " + subsets.size() );

        List<String> tradesList = new LinkedList<>();
        for (  List<IdTrade> itSub : subsets ) {
            String oneSubsetStr = sJson.writeJSON(itSub);
            oneSubsetStr = oneSubsetStr.replace(" ", "-");
            tradesList.add(putCall("etk/trades/", oneSubsetStr));
        }

        int debug = 0;
    }

    public String putCall(String restStr, String input) {
        try {

            URL url = new URL(serverURL + restStr);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("PUT");

            httpCon.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(input);
            out.close();

            BufferedReader inPut = new BufferedReader(
                    new InputStreamReader(httpCon.getInputStream()));

            return IOUtils.toString(inPut);
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    public String getCall(String restStr) {
        try {
            restStr = restStr.replaceAll(" ", "_");

            URL url = new URL(serverURL + restStr);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");

            int responseGet = httpCon.getResponseCode();

            BufferedReader inGet = new BufferedReader(
                    new InputStreamReader(httpCon.getInputStream()));

            return IOUtils.toString(inGet);
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }
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

}
