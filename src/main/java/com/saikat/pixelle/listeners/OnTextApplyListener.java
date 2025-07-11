package com.saikat.pixelle.listeners;

import javafx.scene.paint.Color;

public interface OnTextApplyListener {
    void onApply(String text, Color color, String fontFamily, double fontSize, double x, double y);
}
