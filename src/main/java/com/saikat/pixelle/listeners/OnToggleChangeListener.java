package com.saikat.pixelle.listeners;

import com.saikat.pixelle.constants.ActionType;
import javafx.scene.input.MouseEvent;


public interface OnToggleChangeListener {
    void onToggleChange(MouseEvent event, boolean state, ActionType actionType);
}
