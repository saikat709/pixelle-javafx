package com.saikat.pixelle.components;

import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.listeners.OnToggleChangeListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;

public class ActionToggleButton extends ActionButton {
    private static final String SELECTED_STYLE_CLASS = "selected";
    private OnToggleChangeListener onToggleChangeListener;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public ActionToggleButton(String crop, String text, ActionType actionType) {
        super(crop, text, actionType);

        selected.addListener((obs, oldValue, newValue) -> updateStyleClass(newValue));
    }

    @Override
    public void onMouseClicked(MouseEvent mouseEvent) {
        // Toggle the state
        setSelected(!isSelected());
        // Notify listener
        if (onToggleChangeListener != null) {
            onToggleChangeListener.onToggleChange(mouseEvent, isSelected(), getActionType());
        }
        // Call superclass method if needed
        super.onMouseClicked(mouseEvent);
    }

    public void setOnToggleChangeListener(OnToggleChangeListener listener) {
        this.onToggleChangeListener = listener;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean isSelected) {
        selected.set(isSelected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    private void updateStyleClass(boolean isSelected) {
        if (isSelected) {
            if (!getStyleClass().contains(SELECTED_STYLE_CLASS)) {
                getStyleClass().add(SELECTED_STYLE_CLASS);
            }
        } else {
            getStyleClass().remove(SELECTED_STYLE_CLASS);
        }
    }
}