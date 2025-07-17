package com.saikat.pixelle.editor;

import javafx.scene.paint.Color;

public class BorderCommand extends Command {
    private Double currentVal;
    private Color currentColor;

    private Double previousVal;
    private Double previousColor;

    public BorderCommand(Double currentVal, Double previousVal) {
        this.currentVal = currentVal;
        this.previousVal = previousVal;
    }

    public Double getCurrentVal() {
        return currentVal;
    }

    public Double getPreviousVal() {
        return previousVal;
    }

    public void setCurrentVal(Double currentVal) {
        this.currentVal = currentVal;
    }

    public void setPreviousVal(Double previousVal) {
        this.previousVal = previousVal;
    }
}
