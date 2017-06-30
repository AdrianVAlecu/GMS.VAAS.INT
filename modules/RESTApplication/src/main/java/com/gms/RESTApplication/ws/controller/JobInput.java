package com.gms.RESTApplication.ws.controller;

/**
 * Created by gms on 6/30/2017.
 */
public class JobInput {
    public String query;

    public JobInput(String query) { this.query = query; }

    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
}
