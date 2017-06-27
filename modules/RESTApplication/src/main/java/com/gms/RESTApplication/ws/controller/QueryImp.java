package com.gms.RESTApplication.ws.controller;

/**
 * Created by gms on 6/26/2017.
 */
class QueryImp {
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String query;

    public QueryImp() {
        query = "";
    }

    public QueryImp(String query) {
        this.query = query;
    }

}
