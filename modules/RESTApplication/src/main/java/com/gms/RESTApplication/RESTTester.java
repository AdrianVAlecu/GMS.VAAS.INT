package com.gms.RESTApplication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gms.datasource.TradesDAO;

import java.io.IOException;


public class RESTTester {
    public static void main(String[] args){
            ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        TradesDAO trades = appContext.getBean("tradesDAO",TradesDAO.class);
        try {
            trades.getTradeIds("");
        }catch (IOException e){
            e.printStackTrace();
        }


        System.out.println("First Running application.");
        System.out.println(trades.getClass());
    }
}
