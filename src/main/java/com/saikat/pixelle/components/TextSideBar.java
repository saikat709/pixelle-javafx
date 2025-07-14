package com.saikat.pixelle.components;

import com.saikat.pixelle.listeners.OnTextEditorEvent;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextSideBar extends SideBar {
    private OnTextEditorEvent onTextApplyListener;

    private TextField inputField;
    private ColorPicker colorPicker;
    private ComboBox<String> fontFamilyBox;
    private Slider fontSizeSlider;

    private ColorPicker bgColor;
    private Slider borderRadius;
    private Slider borderWidth;
    private ColorPicker borderColorPicker;

    private Label label;

    private boolean isBorderShow = false;
    private boolean isBackgroundShow = false;

    private final int INITIAL_FONT_SIZE = 20;
    private final String INITIAL_FONT_FAMILY = "Arial";

    public TextSideBar(Label label) {
        this(label, null);
    }

    public TextSideBar(Label label, OnTextEditorEvent onTextApplyListener) {
        this.label = label;
        this.onTextApplyListener = onTextApplyListener;
        label.setFocusTraversable(true);
        super();
        setSpacing(10);
        setPadding(new Insets(10));
        addElements();
    }

    protected void addElements() {
        label.setOnMousePressed(event -> {
            label.setCursor(Cursor.MOVE);
            double startX = event.getSceneX();
            double startY = event.getSceneY();
            double nodeX = label.getTranslateX();
            double nodeY = label.getTranslateY();
            label.setUserData(new double[]{startX, startY, nodeX, nodeY});
        });

        label.setOnMouseReleased(event -> label.setCursor(Cursor.DEFAULT));

        label.setOnMouseDragged(event -> {
            double[] data = (double[]) label.getUserData();
            if (data != null && data.length == 4) {
                double offsetX = event.getSceneX() - data[0];
                double offsetY = event.getSceneY() - data[1];
                label.setTranslateX(data[2] + offsetX);
                label.setTranslateY(data[3] + offsetY);
            }
        });

        renderUI();
        setListeners();
    }

    private void setListeners() {
        inputField.textProperty().addListener((obs, old, newVal) -> applyChanges());
        colorPicker.setOnAction(event -> applyChanges());
        fontFamilyBox.setOnAction(event -> applyChanges());
        fontSizeSlider.valueProperty().addListener((obs, old, newVal) -> applyChanges());
        bgColor.setOnAction(event -> applyChanges());
        borderColorPicker.setOnAction(event -> applyChanges());
        borderWidth.valueProperty().addListener((obs, old, newVal) -> applyChanges());
        borderRadius.valueProperty().addListener((obs, old, newVal) -> applyChanges());
    }

    private void addTextUI() {
        Label title = new Label("Add Text");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0, 0, 10, 0));

        Label textLabel = new Label("Text:");
        inputField = new TextField("Sample Text");
        inputField.setPrefWidth(200);

        HBox color = new HBox(10);
        color.setAlignment(Pos.CENTER_LEFT);
        Label colorLabel = new Label("Color:");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setMinHeight(30);
        color.getChildren().addAll(colorLabel, region, colorPicker);

        Label fontLabel = new Label("Font:");
        fontFamilyBox = new ComboBox<>(FXCollections.observableArrayList(Font.getFamilies()));
        fontFamilyBox.setValue(INITIAL_FONT_FAMILY);
        fontFamilyBox.setPrefWidth(200);

        Label sizeLabel = new Label("Font Size:");
        fontSizeSlider = new Slider(5, 40, INITIAL_FONT_SIZE);
        fontSizeSlider.setShowTickMarks(true);
        fontSizeSlider.setShowTickLabels(true);
        fontSizeSlider.setMajorTickUnit(5);
        fontSizeSlider.setMinorTickCount(4);
        fontSizeSlider.setSnapToTicks(true);
        fontSizeSlider.setPrefWidth(200);

        this.getChildren().addAll(
                title, separator,
                textLabel, inputField,
                color, fontLabel, fontFamilyBox,
                sizeLabel, fontSizeSlider
        );
    }

    private void addBackgroundUI() {
        VBox bgVbox = new VBox(10);

        HBox backgroundTitle = new HBox(10);
        Label backgroundTitleLabel = new Label("Background");
        backgroundTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        CheckBox hasBgCheckBox = new CheckBox();
        backgroundTitle.getChildren().addAll(backgroundTitleLabel, region, hasBgCheckBox);
        backgroundTitle.setAlignment(Pos.CENTER_LEFT);

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0, 0, 10, 0));

        HBox backgroundColor = new HBox(10);
        backgroundColor.setAlignment(Pos.CENTER_LEFT);
        Label bgColorLabel = new Label("Background Color:");
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        bgColor = new ColorPicker(Color.TRANSPARENT);
        bgColor.setMinHeight(30);
        backgroundColor.getChildren().addAll(bgColorLabel, region2, bgColor);

        hasBgCheckBox.setSelected(isBackgroundShow);
        bgVbox.getChildren().addAll(backgroundTitle, separator);

        hasBgCheckBox.setOnAction(event -> {
            isBackgroundShow = hasBgCheckBox.isSelected();
            if (isBackgroundShow) {
                bgVbox.getChildren().add(backgroundColor);
            } else {
                bgVbox.getChildren().remove(backgroundColor);
            }
            applyChanges();
        });

        if (isBackgroundShow) {
            bgVbox.getChildren().add(backgroundColor);
        }

        this.getChildren().add(bgVbox);
    }

    private void addBorderUI() {
        VBox borderVbox = new VBox(10);

        HBox borderTitle = new HBox(10);
        Label borderTitleLabel = new Label("Border");
        borderTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        CheckBox hasBorderCheckBox = new CheckBox();
        borderTitle.getChildren().addAll(borderTitleLabel, region, hasBorderCheckBox);
        borderTitle.setAlignment(Pos.CENTER_LEFT);

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0, 0, 10, 0));

        Label borderRadiusLabel = new Label("Border Radius:");
        borderRadius = new Slider(0, 20, 6);
        borderRadius.setShowTickMarks(true);
        borderRadius.setShowTickLabels(true);
        borderRadius.setMajorTickUnit(5);
        borderRadius.setMinorTickCount(4);
        borderRadius.setSnapToTicks(true);
        borderRadius.setPrefWidth(200);

        Label borderWidthLabel = new Label("Border Width:");
        borderWidth = new Slider(0, 10, 3);
        borderWidth.setShowTickMarks(true);
        borderWidth.setShowTickLabels(true);
        borderWidth.setMajorTickUnit(1);
        borderWidth.setMinorTickCount(4);
        borderWidth.setSnapToTicks(true);
        borderWidth.setPrefWidth(200);

        HBox borderColor = new HBox(10);
        borderColor.setAlignment(Pos.CENTER_LEFT);
        Label borderColorLabel = new Label("Border Color:");
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        borderColorPicker = new ColorPicker(Color.TRANSPARENT);
        borderColorPicker.setMinHeight(30);
        borderColor.getChildren().addAll(borderColorLabel, region2, borderColorPicker);

        hasBorderCheckBox.setSelected(isBorderShow);
        borderVbox.getChildren().addAll(borderTitle, separator);

        hasBorderCheckBox.setOnAction(event -> {
            isBorderShow = hasBorderCheckBox.isSelected();
            if (isBorderShow) {
                borderVbox.getChildren().addAll(
                        borderColor, borderRadiusLabel, borderRadius,
                        borderWidthLabel, borderWidth);
            } else {
                borderVbox.getChildren().removeAll(
                        borderColor, borderRadiusLabel, borderRadius,
                        borderWidthLabel, borderWidth);
            }
            applyChanges();
        });

        if (isBorderShow) {
            borderVbox.getChildren().addAll(
                    borderColor, borderRadiusLabel, borderRadius,
                    borderWidthLabel, borderWidth);
        }

        this.getChildren().add(borderVbox);
    }

    private void addOrDeleteUI() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button addButton = new Button("Add and Close");
        addButton.setOnAction(event -> {
            if (onTextApplyListener != null) {
                onTextApplyListener.onAddAndClose(label);
            }
        });

        Button addAndNext = new Button("Add and Next");
        addAndNext.setOnAction(event -> {
            if (onTextApplyListener != null) {
                onTextApplyListener.onAddAndNext(label);
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            if (onTextApplyListener != null) {
                onTextApplyListener.onDelete(label);
            }
        });

        buttonBox.getChildren().addAll(addButton, addAndNext, deleteButton);
        this.getChildren().add(buttonBox);
    }

    private void renderUI() {
        this.getChildren().clear();
        addTextUI();
        addBackgroundUI();
        addBorderUI();
        addOrDeleteUI();
    }

    private void applyChanges() {
        label.setText(inputField.getText());
        label.setPadding(new Insets(5));
        label.setFont(Font.font(fontFamilyBox.getValue(), fontSizeSlider.getValue()));
        label.setTextFill(colorPicker.getValue());

        if (isBackgroundShow) {
            label.setBackground(new Background(
                    new BackgroundFill(
                            bgColor.getValue(),
                            new CornerRadii(borderRadius.getValue()),
                            Insets.EMPTY
                    )
            ));
        } else {
            label.setBackground(null);
        }

        if (isBorderShow) {
            label.setBorder(new Border(
                    new BorderStroke(
                            borderColorPicker.getValue(),
                            BorderStrokeStyle.SOLID,
                            new CornerRadii(borderRadius.getValue()),
                            new BorderWidths(borderWidth.getValue())
                    )
            ));
        } else {
            label.setBorder(null);
        }
    }

    public void setOnTextApplyListener(OnTextEditorEvent listener) {
        this.onTextApplyListener = listener;
    }
}