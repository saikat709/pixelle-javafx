package com.saikat.pixelle.components;

import com.saikat.pixelle.listeners.OnDrawEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DrawSidebar extends SideBar {
    private OnDrawEvent onDrawListener;
    private Pane drawAndTextPane;

    private ComboBox<String> shapes;
    private ColorPicker strokeColorPicker;
    private Slider strokeWidthSlider;

    private Shape previewShape;
    private double startX, startY;

    private Path path;

    private static final String[] SHAPES = {"Pen", "Line", "Circle", "Rectangle"};
    private static final double INITIAL_STROKE_WIDTH = 2.0;
    private static final Color INITIAL_STROKE_COLOR = Color.BLACK;

    public DrawSidebar(StackPane imageContainerStackpane) {
        this(imageContainerStackpane, null);
    }

    public DrawSidebar(Pane imageContainerStackpane, OnDrawEvent onDrawListener) {
        this.drawAndTextPane = imageContainerStackpane;
        this.onDrawListener = onDrawListener;
        setSpacing(10);
        setPadding(new Insets(10));
        setupListeners();

        System.out.println("Draw Sidebar.");
    }

    @Override
    protected void addElements() {
        Label title = new Label("Draw Shapes");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0, 0, 10, 0));

        HBox shapeSelection = new HBox(10);
        Label shapeLabel = new Label("Shape:");
        shapes = new ComboBox<>();
        shapes.getItems().addAll(SHAPES);
        shapes.setValue(SHAPES[0]);
        shapes.setPrefWidth(200);
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        shapeSelection.getChildren().addAll(shapeLabel, region, shapes);

        HBox strokeColorBox = new HBox(10);
        strokeColorBox.setAlignment(Pos.CENTER_LEFT);
        Label strokeColorLabel = new Label("Stroke Color:");
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        strokeColorPicker = new ColorPicker(INITIAL_STROKE_COLOR);
        strokeColorPicker.setPrefWidth(100);
        strokeColorBox.getChildren().addAll(strokeColorLabel, region2, strokeColorPicker);

        Label strokeWidthLabel = new Label("Stroke Width:");
        strokeWidthSlider = new Slider(1, 10, INITIAL_STROKE_WIDTH);
        strokeWidthSlider.setShowTickMarks(true);
        strokeWidthSlider.setShowTickLabels(true);
        strokeWidthSlider.setMajorTickUnit(1);
        strokeWidthSlider.setMinorTickCount(4);
        strokeWidthSlider.setSnapToTicks(true);
        strokeWidthSlider.setPrefWidth(200);

        this.getChildren().addAll(
                title, separator,
                shapeSelection,
                strokeColorBox,
                strokeWidthLabel,
                strokeWidthSlider
        );
    }

    private void setupListeners() {
        drawAndTextPane.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                startX = event.getX();
                startY = event.getY();

                this.path = new Path();
                this.path.getElements().add(new MoveTo(startX, startY));
                System.out.println("Click point: x = " + startX + ", y = " +  startY);

                String selectedShape = shapes.getValue();
                this.previewShape = createShape(selectedShape, startX, startY, startX, startY);
                if (previewShape != null) {
                    drawAndTextPane.getChildren().add(previewShape);
                }
            }
        });

        drawAndTextPane.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown() && previewShape != null) {
                this.path.getElements().add(new LineTo(event.getX(), event.getY()));
                updatePreviewShape(event.getX(), event.getY());
            }
        });

        drawAndTextPane.setOnMouseReleased(event -> {
            if (previewShape != null) {
                drawAndTextPane.getChildren().remove(previewShape);

                String shapeType = shapes.getValue();
                Shape finalShape = createShape(shapeType, startX, startY, event.getX(), event.getY());

                if (finalShape != null) {
                    // this.drawAndTextPane.getChildren().add(finalShape);
                    if (onDrawListener != null) {
                        onDrawListener.onDraw(finalShape);
                    }
                }
                previewShape = null;
            }
        });

        strokeColorPicker.setOnAction(event -> updateShapeProperties());
        strokeWidthSlider.valueProperty().addListener((obs, old, newVal) -> updateShapeProperties());
    }

    private Shape createShape(String shapeType, double x1, double y1, double x2, double y2) {
        Shape shape = null;
        switch (shapeType) {
            case "Pen":
                path.getElements().add(new LineTo(x2, y2));
                // path.getElements().add(new ClosePath());
                shape = path;
                break;
            case "Line":
                shape = new Line(x1, y1, x2, y2);
                break;
            case "Circle":
                double radius = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                shape = new Circle(x1, y1, radius);
                break;
            case "Rectangle":
                double width  = Math.abs(x2 - x1);
                double height = Math.abs(y2 - y1);
                double minX   = Math.min(x1, x2);
                double minY   = Math.min(y1, y2);
                shape         = new Rectangle(minX, minY, width, height);
                break;
        }
        if (shape != null) {
            shape.setStroke(strokeColorPicker.getValue());
            shape.setStrokeWidth(strokeWidthSlider.getValue());
            shape.setFill(null); // Shapes are outlined by default
        }
        return shape;
    }

    private void updatePreviewShape(double x, double y) {
        if (previewShape == null) return;

        String selectedShape = shapes.getValue();
        drawAndTextPane.getChildren().remove(previewShape);
        previewShape = createShape(selectedShape, startX, startY, x, y);
        if (previewShape != null) {
            drawAndTextPane.getChildren().add(previewShape);
        }
    }

    private void updateShapeProperties() {
        if (previewShape != null) {
            previewShape.setStroke(strokeColorPicker.getValue());
            previewShape.setStrokeWidth(strokeWidthSlider.getValue());
        }
    }

    public void setOnDrawListener(OnDrawEvent listener) {
        this.onDrawListener = listener;
    }
}