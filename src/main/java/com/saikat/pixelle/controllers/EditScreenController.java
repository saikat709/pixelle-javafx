package com.saikat.pixelle.controllers;

import com.saikat.pixelle.components.ActionButton;
import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class EditScreenController {

    private ScreenManager screenManager;

    @FXML public VBox mainVerticalBox;
    @FXML public HBox actionsBottomBar;
    @FXML public FontIcon undoIcon;
    @FXML public FontIcon redoIcon;
    @FXML public ScrollPane bottomButtonsScrollPane;

    public void initialize(){

        screenManager = SingletonFactoryUtil.getInstance(ScreenManager.class);

        // setting up things
        bottomButtonsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // adding buttons
        actionsBottomBar.getChildren().clear();
        ActionButton editButton = new ActionButton("Save", "fas-save", ActionType.SAVE);
        ActionButton drawButton = new ActionButton("Draw", "fas-pen", ActionType.DRAW);
        ActionButton exitButton = new ActionButton("Draw", "fas-cross", ActionType.EXIT);
        exitButton.setIconCode(FontAwesomeSolid.CROP);
        actionsBottomBar.getChildren().addAll(editButton,  drawButton, exitButton);
        for( int i = 0; i < 50; i++){
            ActionButton newBtn = new ActionButton("New", "fas-pen");
            actionsBottomBar.getChildren().add(newBtn);
        }

    }

    @FXML
    public void onBackIconClick(MouseEvent mouseEvent) {
        screenManager.entryScreen();
    }
}
