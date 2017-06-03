package com.gms.datasource.summit;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import summit.etkapi_ws.SU_eToolkitAPIException;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SWrapEToolKit {
	private LinkedBlockingQueue<SWrapEToolKit_ws> etkQueue;
    private SWrapEToolKit_ws etkAPI;
    private ThreadPoolTaskExecutor taskExecutor;

    private Map<String, String> entities;
    private AtomicInteger totalCalls;
    SWrapDOM wrapper;

	public SWrapEToolKit(LinkedBlockingQueue<SWrapEToolKit_ws> etkQueue, int queueSize, SWrapEToolKit_ws etkAPI, ThreadPoolTaskExecutor taskExecutor)  throws SU_eToolkitAPIException, Exception {
        entities = new HashMap<String, String>();
        this.etkQueue = etkQueue;
        this.etkAPI = etkAPI;
        this.taskExecutor = taskExecutor;
        totalCalls = new AtomicInteger(0);

        for ( int i = 0 ; i < queueSize ; i ++ ) {
            this.etkQueue.put(new SWrapEToolKit_ws(this.etkAPI));
        }

        wrapper = new SWrapDOM();
    }

    public void Disconnect(){
        for (Iterator<SWrapEToolKit_ws> it = this.etkQueue.iterator(); it.hasNext(); ) {
            it.next().Disconnect();
        }
        this.etkQueue.clear();
    }

    List<List<String>> executeDBQuery(String sql) throws SU_eToolkitAPIException, InterruptedException {
        String response = "";
        try {
            SWrapEToolKit_ws etkAPI = etkQueue.take();
            response = etkAPI.execute("s_base::DBQuery", "<SummitSQL>" + sql + "</SummitSQL>");
            etkQueue.put(etkAPI);
        }catch (SU_eToolkitAPIException e){
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }

        return wrapper.convertQueryResult(response);

    }

    String executeEntityCreate(String entity) throws SU_eToolkitAPIException, InterruptedException {

        if ( entities.containsKey(entity) ) {
            return entities.get(entity);
        }

        try {
            SWrapEToolKit_ws etkAPI = etkQueue.take();
            String response = etkAPI.execute("s_base::EntityCreate", "<EntityName>" + entity + "</EntityName>");
            entities.put(entity, stripResonseStr(response));
            etkQueue.put(etkAPI);
        }catch (SU_eToolkitAPIException e){
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }


        return entities.get(entity);
    }

    String executeEntityRead(String entity) throws SU_eToolkitAPIException, InterruptedException {

        try {
            SWrapEToolKit_ws etkAPI = etkQueue.take();
            String response = etkAPI.execute("s_base::EntityRead", entity );
            etkQueue.put(etkAPI);
            return stripResonseStr(response);
        }catch (SU_eToolkitAPIException e){
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }

    }

    Vector<String> execute(String function, Vector<String> entities) throws SU_eToolkitAPIException {
        List<Future<String>> futureList = new ArrayList<>();
        Vector<String> results = new Vector<>();

        int maxItems = 500;
        int items = 0;
        try {

            for(String entity : entities) {
                etkExecutor callableTask = new etkExecutor(function, entity);
                Future<String> result = taskExecutor.submit(callableTask);
                futureList.add(result);
                items++;
                if (items > maxItems) {
                    for (Future<String> future : futureList) {
                        results.add(stripResonseStr(future.get()));
                    }
                    futureList.clear();
                    items = 0;
                }
            }
            for (Future<String> future : futureList) {
                results.add(stripResonseStr(future.get()));
            }
            futureList.clear();
        } catch (Exception e){
            e.printStackTrace();
        }


        return results;
    }

    private String stripResonseStr(String response){
        StringBuffer entityStr = new StringBuffer(response);
        entityStr.replace(response.indexOf("</Response>"), response.indexOf("</Response>") + "</Response>".length(), "");
        entityStr.replace(response.indexOf("<Response>"), response.indexOf("<Response>") + "<Response>".length(), "");
        return entityStr.toString();
    }

    private class etkExecutor implements Callable<String> {
        String function;
        String inXML;
        public etkExecutor(String function, String inXML) {
            this.function = function;
            this.inXML = inXML;
        }

        @Override
        public String call() throws Exception {
            StringBuffer outXMLResponse = new StringBuffer();
            Vector<String> messageList = new Vector<String>();

            try {
                SWrapEToolKit_ws etkAPI = etkQueue.take();
                String response = etkAPI.execute(function, inXML );
                etkQueue.put(etkAPI);
                System.out.println("Totat ETK calls: " + totalCalls.incrementAndGet());
                return response;
            }catch (SU_eToolkitAPIException e){
                e.printStackTrace();
                throw e;
            }
        }

    }
}
