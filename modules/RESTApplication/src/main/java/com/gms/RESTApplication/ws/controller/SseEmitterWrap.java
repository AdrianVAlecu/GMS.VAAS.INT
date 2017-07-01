package com.gms.RESTApplication.ws.controller;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by gms on 7/1/2017.
 */
class SseEmitterWrap {
    final static Logger log = Logger.getLogger(TradesController.class);

    private final List<SseEmitter> sseEmitters = new LinkedList<>();

    ScheduledExecutorService sseEmitterPoolExecutor;
    ScheduledFuture<?> sseEmitterHandler;
    Object emitItem;

    public SseEmitter addEmitter(){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        synchronized (sseEmitters) {
            sseEmitters.add(emitter);
            log.info("Registering a stream: " + " size: " + sseEmitters.size() );
        }
        emitter.onCompletion(new SseEmitterRemove(sseEmitters, emitter));

        return emitter;
    }

    public SseEmitterWrap(Object emitItem){
        this.emitItem = emitItem;
        ScheduledExecutorService healthPool = Executors.newScheduledThreadPool(1);
        sseEmitterPoolExecutor = Executors.newScheduledThreadPool(1);
        sseEmitterHandler = sseEmitterPoolExecutor.scheduleWithFixedDelay(new SseEmitterSendState(sseEmitters, emitItem), 0, 5, TimeUnit.SECONDS);

        healthPool.scheduleWithFixedDelay(new WrapHealth(), 0, 1, TimeUnit.SECONDS);
    }

    class WrapHealth implements Runnable {
        public void run() {
            try {
                sseEmitterHandler.get();
            } catch (ExecutionException | InterruptedException e) {
                sseEmitterHandler = sseEmitterPoolExecutor.scheduleWithFixedDelay(new SseEmitterSendState(sseEmitters, emitItem), 0, 5, TimeUnit.SECONDS);
            }
        }
    }
}
