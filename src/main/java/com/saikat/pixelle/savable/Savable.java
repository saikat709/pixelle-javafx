package com.saikat.pixelle.savable;

import java.io.*;

import static com.saikat.pixelle.constants.ConstValues.BASE_DIR;

public abstract class Savable implements Serializable {

    public Savable() {
        if ( !BASE_DIR.exists() ) {
            boolean cr = BASE_DIR.mkdir();
            if ( !cr ) {
                System.err.println("Error creating directory " + BASE_DIR.getAbsolutePath());
            }
        }
    }

    public void saveToDevice() {
        try {
            FileOutputStream fos = new FileOutputStream(getFileToSave());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            System.err.println("Error while saving file: " + this.getName() + ", Due to: " + e.getMessage());
        }
    }

    public Savable loadFromDevice() {
        try {
            File toSave = getFileToSave();
            if ( !toSave.exists() ) {
                boolean creation = toSave.createNewFile();
                if ( !creation ) { throw new IOException("Error while creating file"); }
            }
            FileInputStream fis = new FileInputStream(toSave);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Savable sv = (Savable) ois.readObject();
            ois.close();
            return sv;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while loading file: " + this.getFileToSave().getAbsolutePath() + ", Due to: " + e.getMessage());
            return null;
        }
    }

    private File getFileToSave() {
        return new File(BASE_DIR, getName());
    }

    private String getName() {
        return this.getClass().getSimpleName() + ".im";
    }
}
