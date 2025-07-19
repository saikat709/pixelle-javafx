package com.saikat.pixelle.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BorderRectangle extends Rectangle implements Cloneable {

    private Double borderWidth;
    private Color  borderColor;

    public BorderRectangle() {
        this(0, 0, 0, 0);
    }

    public BorderRectangle(Color color, Double width) {
        this(0, 0, 0, width);
        this.setStroke(color);
    }

    public BorderRectangle(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.setFill(null);
        this.setStroke(Color.WHITE);
        this.setStrokeWidth(5);
    }

    public void setBorderWidth(Double borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Double getBorderWidth() {
        return borderWidth;
    }

    public void setBorderColor(Color borderColor) {
        this.setBorderColor(borderColor);
        this.borderColor = borderColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    @Override
    public BorderRectangle clone(){
        try {
            return (BorderRectangle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
