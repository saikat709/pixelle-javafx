package com.saikat.pixelle.components;

import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.listeners.OnActionButtonClick;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.javafx.Icon;


public class ActionButton extends VBox {
    private OnActionButtonClick onActionButtonClick;
    private final ActionType actionType;
    private final String title;

    public ActionButton(String text, ActionType actionType) {
        super(5);
        this.title = text;
        this.actionType = actionType;
        this.setOnMouseClicked(this::onMouseClicked);

        this.getStyleClass().add("action-button");

        setupButton();
    }

    private void setupButton() {
        Label titleLabel = new Label(title);
        titleLabel.setWrapText(true);

        FontIcon icon  = new FontIcon();
        icon.setIconCode(FontAwesomeSolid.SAVE);

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
