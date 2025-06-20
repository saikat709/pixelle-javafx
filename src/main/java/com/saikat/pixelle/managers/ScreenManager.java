package com.saikat.pixelle.managers;

import com.saikat.pixelle.constants.Screens;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.screens.BaseScreen;
import com.saikat.pixelle.screens.EditScreen;
import com.saikat.pixelle.screens.EntryScreen;
import com.saikat.pixelle.screens.TextToImageScreen;
import com.saikat.pixelle.utils.SingletonFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenManager {
    public int a = 0;
    private Stage stage;
    private double positionX, positionY;

    private AppSettings settings;

    public void initialize(Stage stage) {
        this.stage = stage;
        this.stage.xProperty().addListener((observable, oldValue, newValue) -> {
            positionX = newValue.doubleValue();
        });
        this.stage.yProperty().addListener((observable, oldValue, newValue) -> {
            positionY = newValue.doubleValue();
        });

        SavableManager savableManager = SingletonFactory.getInstance(SavableManager.class);
        this.settings = (AppSettings) savableManager.getSavableClass(AppSettings.class);
    }

    public void check(){
        System.out.println("ScreenManager is running: " + a );
    }

    public void entryScreen() {
        EntryScreen entryScreen = new EntryScreen();
        this.showScreen(entryScreen);
    }

    public void editScreen() {
        EditScreen editScreen = new EditScreen();
        this.showScreen(editScreen);
    }

    public void textToImageScreen() {
        TextToImageScreen textToImageScreen = new TextToImageScreen();
        this.showScreen(textToImageScreen);
    }

    private void showScreen(BaseScreen screen){
        if ( settings != null ) {
            settings.setLastScreen(screen.getScreenName());
        } else {
            System.err.println("AppSettings is null. Maybe the initialize method never called.");
        }
        // stage.hide();
        stage.setScene(screen.getScene());
        if ( positionX != 0 ) stage.setX(positionX);
        if ( positionY != 0 ) stage.setY(positionY);
        stage.show();
    }

    public void exitApp() {
        stage.hide();
        stage.close();
    }

    public void showScreenByName(Screens lastScreen) {
        switch (lastScreen){
            case ENTRY:
                this.entryScreen();
                break;
            case EDIT:
                this.editScreen();
                break;
            case TEXT_TO_IMAGE:
                this.textToImageScreen();
                break;
            case DRAW:
                //
                break;
        }
    }
}
