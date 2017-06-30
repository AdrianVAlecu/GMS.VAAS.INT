package com.gms.RESTApplication.ws.controller;

/**
 * Created by gms on 6/26/2017.
 */
class JobStatus {
    private Long id;
    private JobInput inputParams;

    public JobStatus(JobInput input) {
        this.inputParams = input;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public JobInput getInputParams() {
        return inputParams;
    }
}
