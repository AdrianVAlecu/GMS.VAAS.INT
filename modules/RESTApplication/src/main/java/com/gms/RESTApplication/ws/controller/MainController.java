package com.gms.RESTApplication.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by gms on 6/18/2017.
 */
@Controller
public class MainController {

    @RequestMapping(value="/index", method= RequestMethod.GET)
    private String index(){
        return "index";
    }
}
