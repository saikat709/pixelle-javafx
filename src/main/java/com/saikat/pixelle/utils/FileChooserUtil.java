package com.saikat.pixelle.utils;

import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import javafx.stage.FileChooser;

import java.io.File;

import static com.saikat.pixelle.constants.ConstValues.DOWNLOAD_DIR;

public class FileChooserUtil {
    public static File getSaveFile(String filename){
        File downloadsAppDir = DOWNLOAD_DIR;

        if ( !downloadsAppDir.exists() ) downloadsAppDir.mkdirs();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(downloadsAppDir);
        fileChooser.setTitle("Save Image As");
        fileChooser.setInitialFileName(filename);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File destination = fileChooser.showSaveDialog(null);
        return destination;
    }


    public static File selectAnImage(){
        SavableManager savableManager = SingletonFactoryUtil.getInstance(SavableManager.class);
        AppSettings appSettings = (AppSettings) savableManager.getSavableClass(AppSettings.class);

        String savedPath = appSettings.getLastOpenedDirPath();
        System.out.println("Saved path: " + savedPath);

        File openingDir = savedPath != null ?
                new File(appSettings.getLastOpenedDirPath()) :
                new File(System.getProperty("user.home"));
        System.out.println("Opening directory: " + openingDir.getAbsolutePath());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(openingDir);
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        return selectedFile;
    }
}
