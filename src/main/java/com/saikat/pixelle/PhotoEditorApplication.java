package com.saikat.pixelle;

import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.utils.SingletonFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class PhotoEditorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Pixelle");

        ScreenManager screenManager = SingletonFactory.getInstance(ScreenManager.class);
        screenManager.initialize(stage);
        screenManager.entryScreen();

        getHostServices().showDocument("http://www.stackoverflow.com");

    }

    public static void main(String[] args) {
        launch();
    }
}