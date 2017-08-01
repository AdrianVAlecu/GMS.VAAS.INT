package com.gms.datasource.summit;

import summit.etkapi_ws.SU_eToolkitAPI;
import summit.etkapi_ws.SU_eToolkitAPIException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gms on 5/25/2017.
 */
public class SWrapEToolKit_ws {
    private final String DISCONNECTED_STATUS = "Disconnected";
    private final String CONNECTED_STATUS = "Connected";
    private final String RUNNING_STATUS = "Running";

    private SU_eToolkitAPI etkAPIClass;

    private SU_eToolkitAPI etkAPI;
    private String status = DISCONNECTED_STATUS;
    private Date lastExecutionTime;
    private long idleTime = 10;

    private int maxCallsPerInstance = 100;
    private int maxTry = 3;
    private AtomicInteger etkAPIcalls;

    private String user;
    private String pass;
    private String application;
    private String type;
    private String dbEnv;
    private String extraParams;

    public SWrapEToolKit_ws(){
        etkAPIcalls = new AtomicInteger(0);
        maxCallsPerInstance = 100;
    }

    public SWrapEToolKit_ws(SWrapEToolKit_ws rs){
        this.etkAPIClass = rs.etkAPIClass;
        this.etkAPIcalls = new AtomicInteger(0);
        this.maxCallsPerInstance = rs.maxCallsPerInstance;
        this.user = rs.user;
        this.pass = rs.pass;
        this.application = rs.application;
        this.type = rs.type;
        this.dbEnv = rs.dbEnv;
        this.extraParams = rs.extraParams;

        ScheduledExecutorService disconnectPool = Executors.newScheduledThreadPool(1);
        disconnectPool.scheduleWithFixedDelay(new ETKDisconnect(), 0, 3, TimeUnit.SECONDS);
    }

    public void Connect() throws InterruptedException {
        synchronized (status) {
            Disconnect();

            boolean connectionAdded = false;
            int tries = 0;
            while (!connectionAdded) {
                tries++;
                try {
                    Class<?> c = Class.forName(this.etkAPIClass.getClass().getName());
                    Constructor<?> cons = c.getConstructor();
                    this.etkAPI = (SU_eToolkitAPI) cons.newInstance();
                    this.etkAPI.Connect(this.user, this.pass, this.application, this.type, this.dbEnv, this.extraParams);
                    connectionAdded = true;
                } catch (SU_eToolkitAPIException e) {
                    System.out.print("Failed to start etk: " + e.GetMsg());
                    Thread.sleep(1000);
                    try {
                        this.etkAPI.Disconnect();
                    } catch (SU_eToolkitAPIException e1) {}
                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                    connectionAdded = true;
                } catch (IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                    connectionAdded = true;
                }

                if (tries > maxTry) {
                    throw new InterruptedException("I have tried " + maxTry + " and failed.");
                }
            }
            status = CONNECTED_STATUS;
            lastExecutionTime = new Date();
        }
    }

    public String execute(String function, String inXML) throws SU_eToolkitAPIException {
        StringBuffer outXMLResponse = new StringBuffer();
        Vector<String> messageList = new Vector<String>();

        if ( etkAPI == null || etkAPIcalls.getAndIncrement() > maxCallsPerInstance ){
            System.out.print("Creating connection - existing calls: " + etkAPIcalls.get() + "/" + maxCallsPerInstance );
            etkAPIcalls.set(0);
            try{
                Connect();
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (status){
            status = RUNNING_STATUS;
        }

        try {
            etkAPI.Execute(function, "<Request>" + inXML + "</Request>", outXMLResponse, messageList);
            if ( messageList.size() > 0 ) {
                for (String message : messageList) {
                    System.out.println(message);
                }
            }
        }catch (SU_eToolkitAPIException e){
            e.printStackTrace();
            throw e;
        }

        synchronized (status){
            status = CONNECTED_STATUS;
            lastExecutionTime = new Date();
        }

        return outXMLResponse.toString();
    }

    public void Disconnect(){
        synchronized (status) {
            try {
                if (etkAPI != null) {
                    etkAPI.Disconnect();
                }
            } catch (SU_eToolkitAPIException e) {
                e.printStackTrace();
            }
            status = "Disconnected";
        }
    }

    public SU_eToolkitAPI getEtkAPIClass() {
        return etkAPIClass;
    }

    public void setEtkAPIClass(SU_eToolkitAPI etkAPIClass) {
        this.etkAPIClass = etkAPIClass;
    }

    public SU_eToolkitAPI getEtkAPI() {
        return etkAPI;
    }

    public int getMaxCallsPerInstance() {
        return maxCallsPerInstance;
    }

    public void setMaxCallsPerInstance(int maxCallsPerInstance) {
        this.maxCallsPerInstance = maxCallsPerInstance;
    }

    public void setEtkAPI(SU_eToolkitAPI etkAPI) {
        this.etkAPI = etkAPI;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDbEnv() {
        return dbEnv;
    }

    public void setDbEnv(String dbEnv) {
        this.dbEnv = dbEnv;
    }

    public String getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(String extraParams) {
        this.extraParams = extraParams;
    }


    class ETKDisconnect implements Runnable {
        public void run() {
            synchronized (status) {
                if (status.equals(CONNECTED_STATUS)) {
                    long seconds = ((new Date()).getTime() - lastExecutionTime.getTime()) / 1000;
                    if (seconds > idleTime)
                        Disconnect();
                }
            }
        }
    }

}
