package com.saikat.pixelle.components;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CropOverlay {
    private final double RESIZE_MARGIN = 15;

    private Rectangle cropRect;

    public Rectangle createCropper(StackPane imageContainer) {
        cropRect = new Rectangle(200, 150);

        cropRect.setStroke(Color.BLUE);
        cropRect.setStrokeWidth(3);
        cropRect.setFill(Color.color(0, 0, 1, 0.2));
        cropRect.setCursor(Cursor.MOVE);

        makeDraggableAndResizable(cropRect);
        System.out.println("Pos: " + cropRect.getX() + ", " + cropRect.getY() + " || Image stack: " + imageContainer.getLayoutX() + ", " + imageContainer.getLayoutY());
        cropRect.setX(-200); //imageContainer.getLayoutX());
        System.out.println("Pos: " + cropRect.getX() + ", " + cropRect.getY() + " || Image stack: " + imageContainer.getLayoutX() + ", " + imageContainer.getLayoutY());

        // imageContainer.boundsInLocalProperty().addListener(change);

        imageContainer.getChildren().add(cropRect);
        return cropRect;
    }


    public void removeOverlay(StackPane imageContainer){
        imageContainer.getChildren().remove(cropRect);
    }

    private void makeDraggableAndResizable(Rectangle node) {
        final Delta dragDelta = new Delta();

        node.setOnMousePressed(e -> {
            if (isInResizeZone(node, e.getX(), e.getY(), RESIZE_MARGIN)) {
                node.setCursor(Cursor.SE_RESIZE);
                System.out.println("Click: Resize.");
            } else {
                node.setCursor(Cursor.DEFAULT);
                dragDelta.x = node.getLayoutX() - e.getSceneX();
                dragDelta.y = node.getLayoutY() - e.getSceneY();
                node.setCursor(Cursor.MOVE);
                System.out.println("Click:  Move.");
            }
            System.out.println("Mouse Click.");
        });

        node.setOnMouseDragged(e -> {
            System.out.println("Dragged (Drag): " + node.getCursor());
            if (node.getCursor() == Cursor.SE_RESIZE) {
                double newWidth = e.getX();
                double newHeight = e.getY();
                if (newWidth > 20) node.setWidth(newWidth);
                if (newHeight > 20) node.setHeight(newHeight);
                System.out.println("Dragged -> w: " + newWidth + ", h: " + newHeight);
            } else {
                node.setLayoutX(e.getSceneX() + dragDelta.x);
                node.setLayoutY(e.getSceneY() + dragDelta.y);
                System.out.println("Dragged to: " + dragDelta.toString() );
            }
        });

        node.setOnMouseReleased(e -> node.setCursor(Cursor.DEFAULT));
    }


    private boolean isInResizeZone(Rectangle r, double x, double y, double margin) {
        return (x >= r.getWidth() - margin && y >= r.getHeight() - margin);
    }

    private class Delta {
        double x, y;

        @Override
        public String toString() {
            return "Delta{x=" +x + ", y=" + y + "}";
        }
    }

    public Rectangle getcropRectangle(){
        return cropRect;
    }
}
