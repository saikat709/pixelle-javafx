package com.saikat.pixelle.utils;

import javafx.application.HostServices;

public class Open {
    HostServices hostServices;

    public void initialize(HostServices hostServices){
        this.hostServices = hostServices;
    }

    public void openBrowser(String url){
        try {
            hostServices.showDocument("https://google.com");
            hostServices.notify();
        } catch (Exception e) {
            System.err.println("Error opening browser: " + e.getLocalizedMessage());
        }
    }

}
