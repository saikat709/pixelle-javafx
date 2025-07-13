package com.saikat.pixelle.components;

import com.saikat.pixelle.listeners.OnTextEditorEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextSideBar extends SideBar {
    private OnTextEditorEvent onTextApplyListener;

    private TextField inputField;
    private ColorPicker colorPicker;
    private ComboBox<String> fontFamilyBox;
    private TextField fontSizeField;
    private TextField posXField;
    private TextField posYField;

    private Label label;

    public TextSideBar(Label label) {
        this(label, null);
    }

    public TextSideBar(Label label, OnTextEditorEvent onTextApplyListener) {
        this.onTextApplyListener = onTextApplyListener;
        this.label = label;
        super();
    }

    @Override
    protected void addElements() {
        Label title = new Label("Add Text");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0, 0, 0, 10));

        Label textLabel = new Label("Text:");
        inputField = new TextField("Sample Text");
        inputField.setPrefWidth(200);

        HBox color = new HBox(10);
        color.setAlignment(Pos.CENTER);
        Label colorLabel = new Label("Color:");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setMinHeight(28);
        color.getChildren().addAll(colorLabel, region, colorPicker);


        Label fontLabel = new Label("Font:");
        fontFamilyBox = new ComboBox<>();
        fontFamilyBox.setItems(FXCollections.observableArrayList(Font.getFamilies()));
        fontFamilyBox.setValue("Arial");

        Label sizeLabel = new Label("Font Size:");
        fontSizeField = new TextField("24");
        fontSizeField.setPrefWidth(100);

        Label posXLabel = new Label("Position X:");
        posXField = new TextField("50");
        posXField.setPrefWidth(100);

        Label posYLabel = new Label("Position Y:");
        posYField = new TextField("50");
        posYField.setPrefWidth(100);

        onValueChange(inputField);
        onValueChange(posXField);
        onValueChange(posYField);
        onValueChange(fontSizeField);
        onAction(colorPicker);
        onAction(fontFamilyBox);

        this.getChildren().addAll(
            title, separator,
            textLabel, inputField,
            color,
            fontLabel, fontFamilyBox,
            sizeLabel, fontSizeField,
            posXLabel, posXField,
            posYLabel, posYField
        );

    }

    // TODO: Pending..
    private void addTextUI(){};
    private void addBackgroundUI(){}
    private void addBorderUI(){}
    private void addOrDeleteUI(){

    }

    private void onAction(ComboBoxBase node){
        node.setOnAction(event -> {
            applyChanges();
        });
    }

    private void onValueChange(TextField field) {
        field.setOnKeyPressed(keyEvent -> {
            applyChanges();
        });
    }

    private void applyChanges() {
        label.setText(inputField.getText());
        label.setFont(new Font(fontFamilyBox.getValue(), Double.valueOf(fontSizeField.getText())));
        label.setTextFill(colorPicker.getValue());
    }

    private double validateNumericField(TextField field, double min, double max, double defaultValue) {
        String text = field.getText();
        try {
            double value = Double.parseDouble(text);
            if (value < min || value > max) {
                field.setText(String.valueOf(defaultValue));
                return defaultValue;
            }
            return value;
        } catch (NumberFormatException e) {
            field.setText(String.valueOf(defaultValue));
            return defaultValue;
        }
    }

    public void setOnTextApplyListener(OnTextEditorEvent listener) {
        this.onTextApplyListener = listener;
    }
}