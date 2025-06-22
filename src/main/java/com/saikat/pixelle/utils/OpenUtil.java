package com.saikat.pixelle.utils;

import javafx.application.HostServices;

public class OpenUtil {
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
            hostServices.showDocument(url);
        } catch (Exception e) {
            System.err.println("Error opening browser: " + e.getLocalizedMessage());
        }
    }

}
