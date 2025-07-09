package com.saikat.pixelle.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    private static void createTestImage() {
        int width = 400;
        int height = 300;
        BufferedImage testImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = testImage.createGraphics();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red = (x * 255) / width;
                int green = (y * 255) / height;
                int blue = ((x + y) * 255) / (width + height);

                Color color = new Color(red % 256, green % 256, blue % 256);
                testImage.setRGB(x, y, color.getRGB());
            }
        }

        g2d.setColor(Color.RED);
        g2d.fillOval(50, 50, 80, 80);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(250, 100, 100, 60);

        g2d.setColor(Color.GREEN);
        g2d.fillOval(150, 180, 120, 90);

        g2d.dispose();

        try {
            ImageIO.write(testImage, "png", new File("test_image.png"));
            System.out.println("Test image created: test_image.png");
        } catch (IOException e) {
            System.err.println("Error creating test image");
            e.printStackTrace();
        }
    }
}
