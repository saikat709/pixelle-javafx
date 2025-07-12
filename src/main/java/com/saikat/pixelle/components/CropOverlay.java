package com.saikat.pixelle.utils;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CropOverlayUtil {
    public static Rectangle createCropper(StackPane imageContainer) {
        Rectangle cropRect = new Rectangle(200, 150);

        cropRect.setStroke(Color.BLUE);
        cropRect.setStrokeWidth(2);
        cropRect.setFill(Color.color(0, 0, 1, 0.2)); // semi-transparent
        cropRect.setCursor(Cursor.MOVE);

        makeDraggable(cropRect);
        makeResizable(cropRect);

        imageContainer.getChildren().add(cropRect);
        return cropRect;
    }

    private static void makeDraggable(Rectangle node) {
        final Delta dragDelta = new Delta();

        node.setOnMousePressed(e -> {
            dragDelta.x = node.getLayoutX() - e.getSceneX();
            dragDelta.y = node.getLayoutY() - e.getSceneY();
            node.setCursor(Cursor.MOVE);
        });

        node.setOnMouseDragged(e -> {
            node.setLayoutX(e.getSceneX() + dragDelta.x);
            node.setLayoutY(e.getSceneY() + dragDelta.y);
        });

        node.setOnMouseReleased(e -> node.setCursor(Cursor.DEFAULT));
    }

    private static void makeResizable(Rectangle rect) {
        final double resizeMargin = 8;

        rect.setOnMouseMoved(e -> {
            if (isInResizeZone(rect, e.getX(), e.getY(), resizeMargin)) {
                rect.setCursor(Cursor.SE_RESIZE);
            } else {
                rect.setCursor(Cursor.DEFAULT);
            }
        });

        rect.setOnMouseDragged(e -> {
            if (rect.getCursor() == Cursor.SE_RESIZE) {
                double newWidth = e.getX();
                double newHeight = e.getY();
                if (newWidth > 20) rect.setWidth(newWidth);
                if (newHeight > 20) rect.setHeight(newHeight);
            }
        });
    }

    private static boolean isInResizeZone(Rectangle r, double x, double y, double margin) {
        return (x >= r.getWidth() - margin && y >= r.getHeight() - margin);
    }

    private static class Delta {
        double x, y;
    }
}
