package com.gms.datasource.summit;

import summit.etkapi_ws.SU_eToolkitAPI;
import summit.etkapi_ws.SU_eToolkitAPIException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gms on 5/25/2017.
 */
public class etkAPI_ws {
    private SU_eToolkitAPI etkAPIClass;

    private SU_eToolkitAPI etkAPI;

    private int maxCallsPerInstance;
    private AtomicInteger etkAPIcalls;

    private String user;
    private String pass;
    private String application;
    private String type;
    private String dbEnv;
    private String extraParams;

    public etkAPI_ws(){
        etkAPIcalls = new AtomicInteger(0);
        maxCallsPerInstance = 100;
    }

    public etkAPI_ws(etkAPI_ws rs){
        this.etkAPIClass = rs.etkAPIClass;
        this.etkAPIcalls = new AtomicInteger(0);
        this.maxCallsPerInstance = rs.maxCallsPerInstance;
        this.user = rs.user;
        this.pass = rs.pass;
        this.application = rs.application;
        this.type = rs.type;
        this.dbEnv = rs.dbEnv;
        this.extraParams = rs.extraParams;
    }

    public void Connect() throws InterruptedException {
        Disconnect();

        boolean connectionAdded = false;
        while(!connectionAdded) {
            try {
                Class<?> c = Class.forName(this.etkAPIClass.getClass().getName());
                Constructor<?> cons = c.getConstructor();
                this.etkAPI = (SU_eToolkitAPI) cons.newInstance();
                this.etkAPI.Connect(this.user, this.pass, this.application, this.type, this.dbEnv, this.extraParams);
                connectionAdded = true;
            } catch (SU_eToolkitAPIException e) {
                System.out.print("Failed to start etk: " + e.GetMsg());
                Thread.sleep(5000);
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                connectionAdded = true;
            } catch (IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
                connectionAdded = true;
            }
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

        return outXMLResponse.toString();
    }

    public void Disconnect(){
        try {
            if ( etkAPI != null ) {
                etkAPI.Disconnect();
            }
        } catch (SU_eToolkitAPIException e) {
                e.printStackTrace();
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

}
