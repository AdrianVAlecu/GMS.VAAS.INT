package com.gms.RESTApplication.ws.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * Created by gms on 6/18/2017.
 */
@Controller
public class ControllerMain {
    final static Logger log = Logger.getLogger(ControllerEtk.class);

    private final Jobs jobs = new Jobs();
    SseEmitterWrap sseEmitterJobs = new SseEmitterWrap(jobs);

    @RequestMapping(value="/index", method= RequestMethod.GET)
    private String index(){
        return "index";
    }

    @RequestMapping(path = "/registerJobsEmitter", method = RequestMethod.GET)
    public SseEmitter register() throws IOException {
        return sseEmitterJobs.addEmitter();
    }

    @RequestMapping(value="/addJob", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ModelAndView testPost(@ModelAttribute("query") JobInput input) throws IOException {
        ModelAndView mv = new ModelAndView("index");
        jobs.addJob(new JobStatus(input));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jobsStr = mapper.writeValueAsString(jobs);

        mv.addObject("jobs", jobsStr);
        return mv;
    }

}
