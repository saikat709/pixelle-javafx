package com.saikat.pixelle.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DesktopStyleMenuApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // MenuBar
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem newFile = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem exit = new MenuItem("Exit");

        // Action for "New"
        newFile.setOnAction(e -> showAlert("New File clicked!"));
        exit.setOnAction(e -> primaryStage.close());

        fileMenu.getItems().addAll(newFile, open, new SeparatorMenuItem(), exit);

        // Edit Menu
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(new MenuItem("Cut"), new MenuItem("Copy"), new MenuItem("Paste"));

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");
        about.setOnAction(e -> showAlert("This is a sample desktop-style app."));
        helpMenu.getItems().add(about);

        // Add menus to bar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        BorderPane root = new BorderPane();
        root.setBottom(menuBar);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Desktop Software Menu UI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
