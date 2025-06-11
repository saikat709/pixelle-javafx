package com.saikat.pixelle.components;

import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.listeners.OnActionButtonClick;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;


public class ActionButton extends VBox {
    private OnActionButtonClick onActionButtonClick;
    private final ActionType actionType;
    private final String title;

    private final Double WIDTH = 70.0;
    private final Double HEIGHT = 70.0;

    public ActionButton(String text, ActionType actionType) {
        super(5);
        this.title = text;
        this.actionType = actionType;
        this.setOnMouseClicked(this::onMouseClicked);

        this.getStyleClass().add("action-button");
        this.setMaxSize(WIDTH, HEIGHT);

        setupButton();
    }

    private void setupButton() {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("label");

        FontIcon icon  = new FontIcon();
        icon.setIconCode(FontAwesomeSolid.SAVE);
        icon.setIconSize(24);
        icon.setIconColor(Color.ALICEBLUE);

        this.getChildren().addAll(icon, titleLabel);
    }

    private void onMouseClicked(MouseEvent mouseEvent) {
        if ( onActionButtonClick != null ) {
            onActionButtonClick.onClick(mouseEvent, this.actionType);
        }
    }


    public void setOnActionButtonClick(OnActionButtonClick onActionButtonClick) {
        this.onActionButtonClick = onActionButtonClick;
    }

}
