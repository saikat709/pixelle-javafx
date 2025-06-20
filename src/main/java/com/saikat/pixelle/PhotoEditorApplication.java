package com.saikat.pixelle;

import com.saikat.pixelle.constants.Screens;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.Open;
import com.saikat.pixelle.utils.SingletonFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class PhotoEditorApplication extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("Pixelle");
        stage.setMinWidth(760);
        stage.setMinHeight(520);

        Open open = SingletonFactory.getInstance(Open.class);
        open.initializeWithHostServices(getHostServices());

        ScreenManager screenManager = SingletonFactory.getInstance(ScreenManager.class);
        screenManager.initialize(stage);

        SavableManager savableManager = SingletonFactory.getInstance(SavableManager.class);
        AppSettings settings = (AppSettings) savableManager.getSavableClass(AppSettings.class);
        Screens lastScreen = settings.getLastScreen();

        if ( lastScreen != null ) screenManager.showScreenByName(lastScreen);
        else screenManager.entryScreen();

    }

    public static void main(String[] args) {
        launch();
    }
}