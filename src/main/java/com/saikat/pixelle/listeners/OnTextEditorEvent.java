package com.saikat.pixelle.listeners;

import javafx.scene.control.Label;

public abstract class OnTextEditorEvent {
    public void onDelete(Label label){};
    public void onAddAndClose(Label edited){};
    public void onAddAndNext(Label edited){}
    public void onClickLabel(Label edited){}
}
