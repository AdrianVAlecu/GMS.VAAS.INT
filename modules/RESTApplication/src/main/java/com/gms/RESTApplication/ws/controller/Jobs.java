package com.gms.RESTApplication.ws.controller;

import java.util.*;

/**
 * Created by gms on 6/30/2017.
 */
public class Jobs {

    private long x = 1234567L;
    private long y = 23456789L;
    private Random r = new Random();

    private final List<JobStatus> allJobs = new LinkedList<>();

    public Long addJob(JobStatus jobStatus) {
        long id = x+((long)(r.nextDouble()*(y-x)));
        jobStatus.setId(id);
        allJobs.add(jobStatus);
        return id;
    }

    public void removeJob(Long id){
        for ( JobStatus status : allJobs){
            if (status.getId().compareTo(id) == 0 ) {
                allJobs.remove(status);
                break;
            }
        }
    }

    public List<JobStatus> getAllJobs() {
        return allJobs;
    }

}
