package com.saikat.pixelle.controllers;

import com.saikat.pixelle.PhotoEditorApplication;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class EntryScreenController {

    @FXML
    public VBox buttonsContainer;

    public void initialize(){
        // TODO
    }

    @FXML
    public void checkButtonClick(Event event){
        System.out.println("Check button clicked");
    }

    @FXML
    public void editButtonClick(Event event){
        openFileChooser();
    }

    private void openFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("No file selected");
        }
    }


    @FXML
    public void openHelpLink(){
        System.out.println("Supposed to open help link here.");
    }

    private void showPopup(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setResizable(false);
        alert.showAndWait();
    }
}
