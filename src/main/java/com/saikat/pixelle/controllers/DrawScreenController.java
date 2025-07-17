package com.saikat.pixelle.controllers;

import com.saikat.pixelle.components.CustomMenu;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.util.*;

public class DrawScreenController implements Initializable {

    @FXML public Canvas canvas;
    @FXML public CustomMenu drawMenu;
    @FXML public ColorPicker colorPicker;
    @FXML public Slider strokeSlider;

    private ScreenManager manager;
    private GraphicsContext gc;
    private double startX, startY;
    private String currentTool = "PEN";
    private List<DrawingAction> actions = new ArrayList<>();
    private int actionIndex = -1;
    private List<Image> images = new ArrayList<>();
    private List<double[]> imagePositions = new ArrayList<>();
    private boolean isDraggingImage = false;
    private int draggedImageIndex = -1;

    private static class DrawingAction {
        String tool;
        double x1, y1, x2, y2;
        Color color;
        double strokeWidth;
        String text;
        Image image;

        DrawingAction(String tool, double x1, double y1, double x2, double y2, Color color, double strokeWidth, String text, Image image) {
            this.tool = tool;
            this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
            this.color = color; this.strokeWidth = strokeWidth;
            this.text = text; this.image = image;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Drawing screen controller initialized.");
        manager = SingletonFactoryUtil.getInstance(ScreenManager.class);
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(5);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);

        // Setup menus
        Map<String, List<String>> mp = new LinkedHashMap<>();
        mp.put("File", List.of("Save", "Save As", "Open"));
        mp.put("Tools", List.of("Pen", "Rectangle", "Circle", "Text", "Image", "Clear Screen"));
        drawMenu.setMenus(mp);

        // Initialize canvas
        canvas.setWidth(800);
        canvas.setHeight(600);
        clearCanvas();
    }

    @FXML
    public void onBackIconClick(MouseEvent mouseEvent) {
        manager.entryScreen();
    }

    @FXML
    public void setPenTool() { currentTool = "PEN"; }
    @FXML
    public void setRectangleTool() { currentTool = "RECTANGLE"; }
    @FXML
    public void setCircleTool() { currentTool = "CIRCLE"; }
    @FXML
    public void setTextTool() { currentTool = "TEXT"; }
    @FXML
    public void setColor() {
        gc.setStroke(colorPicker.getValue());
        gc.setFill(colorPicker.getValue());
    }
    @FXML
    public void setStrokeWidth() { gc.setLineWidth(strokeSlider.getValue()); }

    @FXML
    public void openImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            images.add(image);
            imagePositions.add(new double[]{100, 100}); // Default position
            redrawCanvas();
            addAction(new DrawingAction("IMAGE", 100, 100, 0, 0, null, 0, null, image));
        }
    }

    @FXML
    public void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        actions.clear();
        actionIndex = -1;
        images.clear();
        imagePositions.clear();
    }

    @FXML
    public void onUndoClick(MouseEvent event) {
        if (actionIndex >= 0) {
            actionIndex--;
            redrawCanvas();
        }
    }

    @FXML
    public void onRedoClick(MouseEvent event) {
        if (actionIndex < actions.size() - 1) {
            actionIndex++;
            redrawCanvas();
        }
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();

        // Check if clicking on an image for dragging
        for (int i = images.size() - 1; i >= 0; i--) {
            double[] pos = imagePositions.get(i);
            Image img = images.get(i);
            if (startX >= pos[0] && startX <= pos[0] + img.getWidth() &&
                    startY >= pos[1] && startY <= pos[1] + img.getHeight()) {
                isDraggingImage = true;
                draggedImageIndex = i;
                return;
            }
        }

        if (currentTool.equals("TEXT")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Text");
            dialog.setHeaderText("Enter text to add to canvas");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(text -> {
                gc.fillText(text, startX, startY);
                addAction(new DrawingAction("TEXT", startX, startY, (double) 0, (double) 0, (Color) gc.getFill(), gc.getLineWidth(), text, null));
            });
        }
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double currentX = event.getX();
        double currentY = event.getY();

        if (isDraggingImage) {
            imagePositions.get(draggedImageIndex)[0] = currentX;
            imagePositions.get(draggedImageIndex)[1] = currentY;
            redrawCanvas();
        } else if (currentTool.equals("PEN")) {
            gc.beginPath();
            gc.moveTo(startX, startY);
            gc.lineTo(currentX, currentY);
            gc.stroke();
            startX = currentX;
            startY = currentY;
        }
    }

    @FXML
    public void onMouseReleased(MouseEvent event) {
        double endX = event.getX();
        double endY = event.getY();

        if (isDraggingImage) {
            isDraggingImage = false;
            draggedImageIndex = -1;
            addAction(new DrawingAction("IMAGE", imagePositions.get(draggedImageIndex)[0], imagePositions.get(draggedImageIndex)[1],
                    0, 0, null, 0, null, images.get(draggedImageIndex)));
        } else if (currentTool.equals("RECTANGLE")) {
            double x = Math.min(startX, endX);
            double y = Math.min(startY, endY);
            double width = Math.abs(endX - startX);
            double height = Math.abs(endY - startY);
            gc.strokeRect(x, y, width, height);
            addAction(new DrawingAction("RECTANGLE", x, y, width, height, (Color) gc.getStroke(), gc.getLineWidth(), null, null));
        } else if (currentTool.equals("CIRCLE")) {
            double x = Math.min(startX, endX);
            double y = Math.min(startY, endY);
            double width = Math.abs(endX - startX);
            double height = Math.abs(endY - startY);
            gc.strokeOval(x, y, width, height);
            addAction(new DrawingAction("CIRCLE", x, y, width, height, (Color) gc.getStroke(), gc.getLineWidth(), null, null));
        } else if (currentTool.equals("PEN")) {
            addAction(new DrawingAction("PEN", startX, startY, endX, endY, (Color) gc.getStroke(), gc.getLineWidth(), null, null));
        }
    }

    private void addAction(DrawingAction action) {
        if (actionIndex < actions.size() - 1) {
            actions.subList(actionIndex + 1, actions.size()).clear();
        }
        actions.add(action);
        actionIndex++;
    }

    private void redrawCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i <= actionIndex; i++) {
            DrawingAction action = actions.get(i);
            gc.setStroke(action.color != null ? action.color : Color.BLACK);
            gc.setFill(action.color != null ? action.color : Color.BLACK);
            gc.setLineWidth(action.strokeWidth);

            switch (action.tool) {
                case "PEN":
                    gc.beginPath();
                    gc.moveTo(action.x1, action.y1);
                    gc.lineTo(action.x2, action.y2);
                    gc.stroke();
                    break;
                case "RECTANGLE":
                    gc.strokeRect(action.x1, action.y1, action.x2, action.y2);
                    break;
                case "CIRCLE":
                    gc.strokeOval(action.x1, action.y1, action.x2, action.y2);
                    break;
                case "TEXT":
                    gc.fillText(action.text, action.x1, action.y1);
                    break;
                case "IMAGE":
                    gc.drawImage(action.image, action.x1, action.y1);
                    break;
            }
        }

        // Draw images
        for (int i = 0; i < images.size(); i++) {
            gc.drawImage(images.get(i), imagePositions.get(i)[0], imagePositions.get(i)[1]);
        }
    }
}