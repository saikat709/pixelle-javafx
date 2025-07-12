package com.saikat.pixelle.controllers;

import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class DrawScreenController implements Initializable {

    private ScreenManager manager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Drawing screen controller initialized.");
        manager = SingletonFactoryUtil.getInstance(ScreenManager.class);
    }

    @FXML
    public void onBackIconClick(MouseEvent mouseEvent) {
        manager.entryScreen();
    }
}
