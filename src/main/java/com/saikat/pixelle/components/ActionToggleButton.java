package com.saikat.pixelle.components;

import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.listeners.OnToggleChangeListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;

public class ActionToggleButton extends ActionButton {
    private boolean isSelected;
    private OnToggleChangeListener onToggleChangeListener;

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public ActionToggleButton(String crop, String s, ActionType actionType) {
        super(crop, s, actionType);
    }

    @Override
    public void onMouseClicked(MouseEvent mouseEvent) {
        super.onMouseClicked(mouseEvent);
        isSelected = !isSelected;

        if ( isSelected ) this.getStyleClass().add("selected");
        else this.getStyleClass().remove("selected");

        if ( onToggleChangeListener != null )
            onToggleChangeListener.onToggleChange(mouseEvent, isSelected, this.getActionType());

    }

    public void setOnToggleChangeListener(OnToggleChangeListener onToggleChangeListener) {
        this.onToggleChangeListener = onToggleChangeListener;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
