package com.saikat.pixelle.components;

import com.saikat.pixelle.listeners.OnTextApplyListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextSideBar extends SideBar {
    private OnTextApplyListener onTextApplyListener;

    private TextField inputField;
    private ColorPicker colorPicker;
    private ComboBox<String> fontFamilyBox;
    private Slider fontSizeSlider;
    private Slider posXSlider;
    private Slider posYSlider;

    @Override
    protected void addElements() {
        Label title = new Label("Add Text");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0, 0, 0, 10));

        Label textLabel = new Label("Text:");
        inputField = new TextField("Sample Text");
        inputField.setPrefWidth(200);

        Label colorLabel = new Label("Color:");
        colorPicker = new ColorPicker();

        Label fontLabel = new Label("Font:");
        fontFamilyBox = new ComboBox<>();
        fontFamilyBox.setItems(FXCollections.observableArrayList(Font.getFamilies()));
        fontFamilyBox.setValue("Arial");

        Label sizeLabel = new Label("Size:");
        fontSizeSlider = new Slider(8, 72, 24);
        fontSizeSlider.setShowTickLabels(true);
        fontSizeSlider.setShowTickMarks(true);
        fontSizeSlider.setMajorTickUnit(16);

        // X position
        Label posXLabel = new Label("Position X:");
        posXSlider = new Slider(0, 500, 50);

        // Y position
        Label posYLabel = new Label("Position Y:");
        posYSlider = new Slider(0, 500, 50);

        // Apply Button
        Button applyBtn = getApplyBtn();

        this.getChildren().addAll(
                title, separator,
                textLabel, inputField,
                colorLabel, colorPicker,
                fontLabel, fontFamilyBox,
                sizeLabel, fontSizeSlider,
                posXLabel, posXSlider,
                posYLabel, posYSlider,
                applyBtn
        );
    }

    private Button getApplyBtn() {
        Button applyBtn = new Button("Apply Text");
        applyBtn.setOnAction(e -> {
            if (onTextApplyListener != null) {
                onTextApplyListener.onApply(
                        inputField.getText(),
                        colorPicker.getValue(),
                        fontFamilyBox.getValue(),
                        fontSizeSlider.getValue(),
                        posXSlider.getValue(),
                        posYSlider.getValue()
                );
            }
        });
        return applyBtn;
    }

    public void setOnTextApplyListener(OnTextApplyListener listener) {
        this.onTextApplyListener = listener;
    }
}
