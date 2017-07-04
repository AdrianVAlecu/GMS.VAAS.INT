package com.gms.RESTApplication.ws.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

/**
 * Created by gms on 7/1/2017.
 */
class SseEmitterSendState implements Runnable {
    List<SseEmitter> sseEmitters;
    Object emitItem;

    public SseEmitterSendState(List<SseEmitter> sseEmitters, Object emitItem) {
        this.sseEmitters = sseEmitters;
        this.emitItem = emitItem;
    }

    public void run() {
        ControllerEtk.log.info("Sending message emitters: " + this.sseEmitters.size());

        synchronized (this.sseEmitters) {
            for (SseEmitter emitter : this.sseEmitters) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                    String jobsStr = mapper.writeValueAsString(emitItem);
                    emitter.send(jobsStr, MediaType.APPLICATION_JSON);
                } catch (IOException e) {
                    this.sseEmitters.remove(emitter);
                    ControllerEtk.log.info("IOException - removing jobs emitters: " + this.sseEmitters.size());
                }
            }
        }
    }
}
