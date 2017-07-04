package com.gms.RESTApplication.ws.controller;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * Created by gms on 7/1/2017.
 */
class SseEmitterRemove implements Runnable {
    List<SseEmitter> sseEmitters;
    SseEmitter emitter;

    public SseEmitterRemove(List<SseEmitter> sseEmitters, SseEmitter emitter) {
        this.sseEmitters = sseEmitters;
        this.emitter = emitter;
    }

    public void run() {
        synchronized (this.sseEmitters) {
            if (this.sseEmitters.contains(emitter)) {
                this.sseEmitters.remove(emitter);
                ControllerEtk.log.info("Disconnecting: emitters: " + this.sseEmitters.size());
            }
        }
    }
}
