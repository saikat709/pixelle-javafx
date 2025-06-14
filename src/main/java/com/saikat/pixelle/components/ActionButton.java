package com.saikat.pixelle.components;

import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.listeners.OnActionButtonClick;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;


public class ActionButton extends VBox {
    private OnActionButtonClick onActionButtonClick;
    private final ActionType actionType;

    private final Double WIDTH = 70.0 + 5.0;
    private final Double HEIGHT = 70.0;

    private final StringProperty title = new SimpleStringProperty("Title");
    private final StringProperty iconLiteral = new SimpleStringProperty("fas-save");
    private final IntegerProperty iconSize = new SimpleIntegerProperty(24);

    Label titleLabel;
    FontIcon icon;

    public ActionButton(){
        this(null, null, null);
    }

    public ActionButton(String text, ActionType actionType) {
        this(text, null, actionType);
    }

    public ActionButton(String title, String iconLiteral, ActionType actionType) {
        super(56);
        if ( title != null ) this.title.set(title);
        this.actionType = actionType;
        if ( iconLiteral != null ) this.iconLiteral.set(iconLiteral);
        this.setOnMouseClicked(this::onMouseClicked);

        this.getStyleClass().add("action-button");
        this.setMaxSize(WIDTH, HEIGHT);
        this.setSpacing(2);
        setupButton();
    }

    public ActionButton(String title, String iconLiteral) {
        this(title, iconLiteral, null);
    }

    private void setupButton() {
        titleLabel = new Label(title.getValue());
        titleLabel.getStyleClass().add("label");
        icon = new FontIcon();
        icon.setIconLiteral(iconLiteral.getValue());
        icon.setIconSize(iconSize.get());
        icon.setIconColor(Color.ALICEBLUE);

        this.getChildren().addAll(icon, titleLabel);
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        if ( onActionButtonClick != null ) {
            onActionButtonClick.onClick(mouseEvent, this.actionType);
        }
    }

    public void setOnActionButtonClick(OnActionButtonClick onActionButtonClick) {
        this.onActionButtonClick = onActionButtonClick;
    }

    public String getIconLiteral() {
        return iconLiteral.get();
    }

    public void setIconLiteral(String iconLiteralProperty) {
        this.setIcon(iconLiteralProperty);
    }

    public void setIconCode(FontAwesomeSolid iconName){
        System.out.println(iconName.getCode());
        this.icon.setIconCode(iconName);
    }

    public void setIcon(String iconLiteral){
        this.iconLiteral.set(iconLiteral);
        this.icon.setIconLiteral(iconLiteral);
    }

    public String getTitle() {
        return this.title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
        this.titleLabel.setText(title);
    }

    public Integer getIconSize() {
        return iconSize.get();
    }

    public void setIconSize(Integer iconSizeProperty) {
        this.iconSize.set(iconSizeProperty);
        this.icon.setIconSize(iconSizeProperty);
    }

}
