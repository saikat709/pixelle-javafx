package com.saikat.pixelle;

import com.saikat.pixelle.constants.Screens;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.OpenUtil;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class PhotoEditorApplication extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("Pixelle");
        stage.setMinWidth(760);
        stage.setMinHeight(520);

        OpenUtil open = SingletonFactoryUtil.getInstance(OpenUtil.class);
        open.initializeWithHostServices(getHostServices());

        ScreenManager screenManager = SingletonFactoryUtil.getInstance(ScreenManager.class);
        screenManager.initialize(stage);

        SavableManager savableManager = SingletonFactoryUtil.getInstance(SavableManager.class);
        AppSettings settings = (AppSettings) savableManager.getSavableClass(AppSettings.class);
        Screens lastScreen = settings.getLastScreen();

        if ( lastScreen != null ) screenManager.showScreenByName(lastScreen);
        else screenManager.entryScreen();

    }

    public static void main(String[] args) {
        launch();
    }
}