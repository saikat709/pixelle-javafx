package com.saikat.pixelle.listeners;

import com.saikat.pixelle.constants.ActionType;
import javafx.event.Event;

public interface OnActionButtonClick {
    public void onClick(Event event, ActionType actionType);
}

