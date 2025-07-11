package com.saikat.pixelle.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public abstract class SideBar extends VBox {

    public SideBar(){
        this.setWidth(250);
        this.setAlignment(Pos.TOP_LEFT);
        this.setSpacing(10.0);
        this.setPadding(new Insets(10));
        addElements();
    }

    protected abstract void addElements();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{Width=" + this.getWidth() + ", Height" + this.getHeight() +  "}";
    }
}
