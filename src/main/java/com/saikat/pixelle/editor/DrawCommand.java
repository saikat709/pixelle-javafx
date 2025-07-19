package com.saikat.pixelle.editor;

import javafx.scene.shape.Shape;

public class DrawCommand extends Command {
    private Shape shape;

    public DrawCommand(Shape shape) {
        this.shape = shape;
    }

    public Shape getShape() {
        return shape;
    }
}
