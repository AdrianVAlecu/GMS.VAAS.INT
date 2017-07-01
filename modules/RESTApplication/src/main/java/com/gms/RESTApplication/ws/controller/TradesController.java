package com.gms.RESTApplication.ws.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.datasource.IdTrade;
import com.gms.datasource.Trade;
import com.gms.datasource.DAOTrades;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by GMS on 05/03/2017.
 */
@RestController
@RequestMapping(value="/")
@ImportResource({"classpath:summit-spring.xml"})
@PropertySource(value={"classpath:summit.app.properties"})
public class TradesController {
    final static Logger log = Logger.getLogger(TradesController.class);

    private final Jobs jobs = new Jobs();

    private final List<SseEmitter> sseEmitters = new LinkedList<>();
    ScheduledExecutorService scheduledPool;
    ScheduledFuture<?> handle;

    private int counter = 1;

    private class SendStateRunnable implements Runnable {
        public void run() {
            log.info("Sending state message " + counter++ + ", emitters: " + sseEmitters.size() );

            synchronized ( sseEmitters ){
                for ( SseEmitter emitter : sseEmitters ) {
                    try{
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                        String jobsStr = mapper.writeValueAsString(jobs);
                        emitter.send(jobsStr, MediaType.APPLICATION_JSON);
                    }catch(IOException e) {
                        sseEmitters.remove(emitter);
                        log.info("IOException - removing: " + counter + ", emitters: " + sseEmitters.size());
                    }
                }
            }
        }
    }

    private class RemoveEmitterRunnable implements Runnable {
        SseEmitter emitter;
        public RemoveEmitterRunnable(SseEmitter emitter) {
            this.emitter = emitter;
        }
        public void run() {
            synchronized ( sseEmitters ){
                if ( sseEmitters.contains(emitter) ) {
                    sseEmitters.remove(emitter);
                    log.info("Disconnecting: " + counter + ", emitters: " + sseEmitters.size());
                }
            }
        }
    }

    private class CheckThredsHealth implements Runnable {
        public void run() {
            try {
                handle.get();
            }catch (ExecutionException | InterruptedException e) {
                handle = scheduledPool.scheduleWithFixedDelay(new SendStateRunnable(), 0, 5, TimeUnit.SECONDS);
            }
        }
    }


    @Resource
    private DAOTrades DAOTrades;

    public TradesController(){
        ScheduledExecutorService healthPool = Executors.newScheduledThreadPool(1);
        scheduledPool = Executors.newScheduledThreadPool(4);
        handle = scheduledPool.scheduleWithFixedDelay(new SendStateRunnable(), 0, 5, TimeUnit.SECONDS);

        healthPool.scheduleWithFixedDelay(new CheckThredsHealth(), 0, 1, TimeUnit.SECONDS);
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public SseEmitter register() throws IOException {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        synchronized (sseEmitters) {
            sseEmitters.add(emitter);
            log.info("Registering a stream: " + " size: " + sseEmitters.size() );
        }
        emitter.onCompletion(new RemoveEmitterRunnable(emitter));

        return emitter;
    }

    @RequestMapping(value="/trade/{id}", method= RequestMethod.GET)
    public Map<String, IdTrade> getTrade(@PathVariable("id") String query) throws IOException, JsonProcessingException {
        return DAOTrades.getTradeIds("and TradeId in ('" + query + "')");
    }

    @RequestMapping(value="/trades", method=RequestMethod.GET)
    public Map<String, Trade> getTrades(@PathVariable Map<String, IdTrade> tradeIds) throws IOException, JsonProcessingException {
        return DAOTrades.getTrades(tradeIds);
    }

    @RequestMapping(value="/addJob", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody ModelAndView testPost(@ModelAttribute("query") JobInput input) throws IOException {
        ModelAndView mv = new ModelAndView("index");
        Long id = new Long(input.getQuery().hashCode());
        jobs.addJob(new JobStatus(input));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jobsStr = mapper.writeValueAsString(jobs);

        mv.addObject("jobs", jobsStr);
        return mv;
    }
}
