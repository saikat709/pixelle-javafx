package com.saikat.pixelle.savable;

import java.io.*;

public class Savable implements Serializable {
    protected File BASE_DIR = new  File(System.getProperty("user.dir"), "./pixelle");

    public void saveToDevice() throws IOException {
        FileOutputStream fos = new FileOutputStream(getFileToSave());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }

    public Savable loadFromDevice() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(getFileToSave());
        ObjectInputStream ois = new ObjectInputStream(fis);
        Savable sv = (Savable) ois.readObject();
        ois.close();
        return sv;
    }

    private File getFileToSave() {
        return new File(BASE_DIR, getName());
    }

    private String getName() {
        return this.getClass().getSimpleName() + ".im";
    }
}
