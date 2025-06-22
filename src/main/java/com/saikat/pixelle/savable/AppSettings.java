package com.saikat.pixelle.savable;

import com.saikat.pixelle.constants.Screens;

public class AppSettings extends Savable {
    private String  lastOpenedDirPath;
    private String  lastImageGenPrompt;
    private Screens lastScreen;

    public String getLastOpenedDirPath() {
        return lastOpenedDirPath;
    }

    public void setLastOpenedDirPath(String lastOpenedDirPath) {
        if( lastOpenedDirPath == null || lastOpenedDirPath.equals(this.lastOpenedDirPath) ) return;
        this.lastOpenedDirPath = lastOpenedDirPath;
        this.saveToDevice();
    }

    public Screens getLastScreen() {
        return lastScreen;
    }

    public void setLastScreen(Screens lastScreen) {
        if ( lastScreen == null || lastScreen == this.lastScreen ) return;
        this.lastScreen = lastScreen;
        this.saveToDevice();
    }

    public String getLastImageGenPrompt() {
        return lastImageGenPrompt;
    }

    public void setLastImageGenPrompt(String lastImageGenPrompt) {
        if ( lastImageGenPrompt == null || lastImageGenPrompt.equals(this.lastImageGenPrompt) ) return;
        this.lastImageGenPrompt = lastImageGenPrompt;
        this.saveToDevice();
    }
}
