package com.gms.RESTApplication.ws.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by GMS on 05/03/2017.
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages={"com.gms.RESTApplication.ws"})
public class WebMvcConfig extends WebMvcConfigurerAdapter {
}
