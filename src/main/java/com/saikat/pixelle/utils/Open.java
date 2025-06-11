package com.saikat.pixelle.utils;

import javafx.application.HostServices;

public class Open {
    private HostServices hostServices;

    public void initializeWithHostServices(HostServices hostServices){
        this.hostServices = hostServices;
    }

    public void openBrowser(String url){
        if ( hostServices == null ){
            System.err.println("Host Services are not initialized");
            return;
        }

        try {
            hostServices.showDocument("https://google.com");
            hostServices.notify();
        } catch (Exception e) {
            System.err.println("Error opening browser: " + e.getLocalizedMessage());
        }
    }

}
