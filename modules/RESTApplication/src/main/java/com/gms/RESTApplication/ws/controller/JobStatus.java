package com.gms.RESTApplication.ws.controller;

/**
 * Created by gms on 6/26/2017.
 */
public class JobStatus {
    private Long id;
    private JobInput inputParams;

    public void setStatus(JobStatusEnum status) {
        this.status = status;
    }

    public void setJobInfo(String jobInfo) {
        this.jobInfo = jobInfo;
    }

    private JobStatusEnum status;
    private String jobInfo;

    public enum JobStatusEnum {
        Running("Running"),
        FAILED("FAILED"),
        ERROR("ERROR"),
        EMPTY("EMPTY"),
        SUCCESS("Success");

        private String name = "";
        JobStatusEnum(String name) { this.name = name; }
        public String toString(){ return name; }
    }

    public JobStatus(JobInput input) {
        this.inputParams = input;
        status = JobStatusEnum.Running;
        jobInfo = "Submitted";
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
