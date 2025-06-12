package com.saikat.pixelle.savable;

public class AppSettings extends Savable {
    private String lastOpenedDirPath;

    public String getLastOpenedDirPath() {
        return lastOpenedDirPath;
    }

    public void setLastOpenedDirPath(String lastOpenedDirPath) {
        this.lastOpenedDirPath = lastOpenedDirPath;
        this.saveToDevice();
    }

}
