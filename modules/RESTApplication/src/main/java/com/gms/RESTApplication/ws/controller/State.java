package com.gms.RESTApplication.ws.controller;

/**
 * Created by gms on 6/28/2017.
 */
public class State {

    public State(String text){
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String text;
}
