package com.saikat.pixelle;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class MainController {
    @FXML private Label welcomeText;

    @FXML protected void onTestButtonClick() {
        Stage stage = new Stage();

        stage.setTitle("My New Stage Title");

        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setMinWidth(450);
        stage.setMinHeight(450);
        // stage.setMaximized(true);
        // stage.setFullScreen(true);

        stage.setFullScreenExitHint("Press ESC to exit full screen mode");
        stage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
        });

        Slider sd = new Slider(0, 100, 0);
        sd.setShowTickMarks(true);
        sd.setOnDragDone(event -> {
            System.out.println("Drag done");
            System.out.println(sd.getValue());
        });

        sd.setOnDragDetected(event -> {
                    System.out.println("Drag detected + " + sd.getValue());
                    sd.startFullDrag();
                }
        );

        stage.setScene(new Scene(sd, 450, 450));

        stage.show();
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}