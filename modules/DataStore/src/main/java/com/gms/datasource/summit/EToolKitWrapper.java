package com.gms.datasource.summit;

import summit.etkapi_ws.SU_eToolkitAPI;

public class EToolKitWrapper {
	private SU_eToolkitAPI etkAPI;
	private String summitUser;
	private String summitPass;
	private String summitApplication;
	private String summitType;
	private String summitDbEnv;
	private String summitExtraParams;

	public EToolKitWrapper(SU_eToolkitAPI etkAPI){
	    this.etkAPI = etkAPI;
    }
    public void setSummitUser(String summitUser){
        this.summitUser = summitUser;
    }
    
    public void setSummitPass(String summitPass){
        this.summitPass = summitPass;
    }

    public void setSummitApplication(String summitApplication){
        this.summitApplication = summitApplication;
    }
    
    public void setSummitType(String summitType){
        this.summitType = summitType;
    }
    
    public void setSummitDbEnv(String summitDbEnv){
        this.summitDbEnv = summitDbEnv;
    }

    public void setSummitExtraParams(String summitExtraParams){
        this.summitExtraParams = summitExtraParams;
    }
}
