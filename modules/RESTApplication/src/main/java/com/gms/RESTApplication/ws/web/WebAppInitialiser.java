package com.gms.RESTApplication.ws.web;

import com.gms.RESTApplication.ws.web.WebMvcConfig;
import com.gms.datasource.TradesDAO;
import com.gms.datasource.summit.SummitTradesDAO;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by GMS on 05/03/2017.
 */
public class WebAppInitialiser implements WebApplicationInitializer{

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException{
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();

        rootContext.register(TradesDAO.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        AnnotationConfigWebApplicationContext dispatcherServlet =
                new AnnotationConfigWebApplicationContext();
        dispatcherServlet.register(WebMvcConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
                new DispatcherServlet(dispatcherServlet));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
