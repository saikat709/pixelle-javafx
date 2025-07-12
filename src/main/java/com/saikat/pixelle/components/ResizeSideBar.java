package com.saikat.pixelle.components;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ResizeSideBar extends SideBar {

    private double originalWidth;
    private double originalHeight;
    private OnResizeListener resizeListener;

    public ResizeSideBar(double originalWidth, double originalHeight) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        addElements();
    }

    @Override
    protected void addElements() {
        Label title = new Label("Resize Image");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0, 0, 0, 10));

        // Width
        Label widthLabel = new Label("Width:");
        Slider widthSlider = new Slider(10, originalWidth * 2, originalWidth);
        widthSlider.setShowTickLabels(true);
        widthSlider.setShowTickMarks(true);

        // Height
        Label heightLabel = new Label("Height:");
        Slider heightSlider = new Slider(10, originalHeight * 2, originalHeight);
        heightSlider.setShowTickLabels(true);
        heightSlider.setShowTickMarks(true);

        // Apply Button
        Button applyButton = new Button("Apply Resize");
        applyButton.setOnAction(e -> {
            if (resizeListener != null) {
                double newWidth = widthSlider.getValue();
                double newHeight = heightSlider.getValue();
                resizeListener.onResize(newWidth, newHeight);
            }
        });

        this.getChildren().addAll(
                title, separator,
                widthLabel, widthSlider,
                heightLabel, heightSlider,
                applyButton
        );
    }

    public void setOnResizeListener(OnResizeListener listener) {
        this.resizeListener = listener;
    }

    public interface OnResizeListener {
        void onResize(double newWidth, double newHeight);
    }
}
