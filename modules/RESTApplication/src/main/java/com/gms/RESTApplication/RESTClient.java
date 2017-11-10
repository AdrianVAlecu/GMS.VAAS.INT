package com.gms.RESTApplication;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.RESTApplication.ws.RESTWorkflow;
import com.gms.RESTApplication.ws.controller.JobInput;
import com.gms.RESTApplication.ws.controller.JobStatus;
import com.gms.RESTApplication.ws.controller.Jobs;
import com.gms.datasource.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

public class RESTClient extends SpringBootServletInitializer {

    public static void main(String[] args){

        ApplicationContext appContext = new ClassPathXmlApplicationContext("summit-spring.xml");

        if (appContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext)appContext).registerShutdownHook();
        }
        try

        {
            Jobs jobs = new Jobs();
            JobStatus newJob = new JobStatus(new JobInput("and dmOwnerTable in ('SWAP', 'MM', 'MUST_TR')"));
            jobs.addJob(newJob);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            String jobsStr = mapper.writeValueAsString(jobs);

            RESTWorkflow workflow = new RESTWorkflow(newJob);
        }
        catch(JsonProcessingException e)
        {
            e.printStackTrace();
        }
        System.out.println("First Running application.");
    }

}
