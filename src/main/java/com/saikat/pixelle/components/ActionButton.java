package com.saikat.pixelle.components;

import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.listeners.OnActionButtonClick;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class ActionButton extends VBox {
    private OnActionButtonClick onActionButtonClick;

    private final Double WIDTH  = 80.0;
    private final Double HEIGHT = 80.0;

    private final StringProperty title = new SimpleStringProperty("Title");
    private final StringProperty iconLiteral = new SimpleStringProperty("fas-save");
    private final IntegerProperty iconSize = new SimpleIntegerProperty(24);
    private final ObjectProperty<ActionType> actionType = new SimpleObjectProperty<>(ActionType.NONE);


    protected Label titleLabel;
    protected FontIcon icon;

    public ActionButton(){
        this(null, null, null);
    }

    public ActionButton(String text, ActionType actionType) {
        this(text, null, actionType);
    }

    public ActionButton(String title, String iconLiteral, ActionType actionType) {
        super(56);
        if ( title != null ) this.title.set(title);
        this.actionType.set(actionType);
        if ( iconLiteral != null ) this.iconLiteral.set(iconLiteral);
        this.setOnMouseClicked(this::onMouseClicked);

        this.getStyleClass().add("action-button");
        this.setMinSize(WIDTH, HEIGHT);
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
        titleLabel.setEllipsisString("...");
        titleLabel.setTextOverrun(OverrunStyle.ELLIPSIS);

        icon = new FontIcon();
        icon.setIconLiteral(iconLiteral.getValue());
        icon.setIconSize(iconSize.get());
        icon.setIconColor(Color.ALICEBLUE);

        this.getChildren().addAll(icon, titleLabel);
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        if ( onActionButtonClick != null )  onActionButtonClick.onClick(mouseEvent, this.actionType.get());
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

    public ActionType getActionType() {
        return actionType.get();
    }

    public void setActionType(ActionType actionType) {
        this.actionType.set(actionType);
    }


    @Override
    public boolean equals(Object obj) {
        return ( (ActionButton) obj ).getActionType() == this.getActionType();
    }

    @Override
    public String toString() {
        return "ActionButton{actionType=" + this.getActionType() + '}';
    }
}
