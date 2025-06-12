package com.saikat.pixelle.managers;

import com.saikat.pixelle.screens.EditScreen;
import com.saikat.pixelle.screens.EntryScreen;
import com.saikat.pixelle.screens.TextToImageScreen;
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
        this.showScreen(entryScreen.getScene());
    }

    public void editScreen() {
        EditScreen editScreen = new EditScreen();
        this.showScreen(editScreen.getScene());
    }

    public void textToImageScreen() {
        TextToImageScreen textToImageScreen = new TextToImageScreen();
        this.showScreen(textToImageScreen.getScene());
    }

    private void showScreen(Scene scene){
        stage.hide();
        stage.setMinWidth(760);
        stage.setMinHeight(520);
        stage.setScene(scene);
        if ( positionX != 0 ) stage.setX(positionX);
        if ( positionY != 0 ) stage.setY(positionY);
        stage.show();
    }

    public void exitApp() {
        stage.hide();
        stage.close();
    }
}
