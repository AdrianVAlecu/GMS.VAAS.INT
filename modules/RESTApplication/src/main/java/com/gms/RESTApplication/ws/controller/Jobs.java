package com.gms.RESTApplication.ws.controller;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by gms on 6/30/2017.
 */
public class Jobs {

    private static final AtomicLong LAST_TIME_MS = new AtomicLong();

    private final List<JobStatus> allJobs = new LinkedList<>();

    public Long addJob(JobStatus jobStatus) {
        jobStatus.setId(uniqueCurrentTimeMS());
        synchronized (allJobs) {
            allJobs.add(jobStatus);
        }
        return jobStatus.getId();
    }

    public void removeJob(Long id){
        synchronized (allJobs) {
            for (JobStatus status : allJobs) {
                if (status.getId().compareTo(id) == 0) {
                    allJobs.remove(status);
                    break;
                }
            }
        }
    }

    public List<JobStatus> getAllJobs() {
        return allJobs;
    }

    public static long uniqueCurrentTimeMS(){
        long now = System.currentTimeMillis();
        while(true) {
            long lastTime = LAST_TIME_MS.get();
            if ( lastTime >= now )
                now = lastTime + 1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now)) {
                return now;
            }
        }
    }


}
