package com.saikat.pixelle.controllers;

import com.saikat.pixelle.components.ActionButton;
import com.saikat.pixelle.constants.ActionType;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class EditScreenController {

    @FXML
    public VBox mainVerticalBox;

    public void initialize(){
        mainVerticalBox.getChildren().clear();
        ActionButton editButton = new ActionButton("Save", ActionType.SAVE);
        ActionButton drawButton = new ActionButton("Draw", ActionType.DRAW);
        ActionButton exitButton = new ActionButton("Draw", ActionType.EXIT);
        mainVerticalBox.getChildren().addAll(editButton,  drawButton, exitButton);
    }
}
