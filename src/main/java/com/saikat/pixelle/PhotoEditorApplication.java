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

        System.setProperty("glass.win.uiScale", "100%"); // Force Glass to use 100% scale
        System.setProperty("prism.forcehiDPI", "false"); // Disable HiDPI if it's causing issues

        OpenUtil open = SingletonFactoryUtil.getInstance(OpenUtil.class);
        open.initializeWithHostServices(getHostServices());

        ScreenManager screenManager = SingletonFactoryUtil.getInstance(ScreenManager.class);
        screenManager.initialize(stage);

        screenManager.startApplication();
    }

    public static void main(String[] args) {
        launch();
    }
}