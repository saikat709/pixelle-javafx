package com.saikat.pixelle.managers;

import com.saikat.pixelle.screens.EntryScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenManager {
    public int a = 0;
    private Stage stage;
    private double positionX, positionY;

    public void initialize(Stage stage){
        this.stage = stage;
        this.stage.xProperty().addListener((observable, oldValue, newValue) -> {
            positionX = newValue.doubleValue();
        });
        this.stage.yProperty().addListener((observable, oldValue, newValue) -> {
            positionY = newValue.doubleValue();
        });
    }

    public void check(){
        System.out.println("ScreenManager is running: " + a );
    }

    public void entryScreen() {
        EntryScreen entryScreen = new EntryScreen();
        if ( positionX != 0 ) stage.setX(positionX);
        if ( positionY != 0 ) stage.setY(positionY);
        this.showScreen(entryScreen.getScene());
    }

    private void showScreen(Scene scene){
        stage.hide();
        stage.setMinWidth(720);
        stage.setScene(scene);
        stage.show();
    }
}
