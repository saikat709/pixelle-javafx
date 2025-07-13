package com.saikat.pixelle.components;

import com.saikat.pixelle.listeners.OnAdjustmentChange;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdjustmentsSideBar extends SideBar {

    private OnAdjustmentChange onAdjustmentChange;

    public AdjustmentsSideBar() {
        this.onAdjustmentChange = null;
    }

    public AdjustmentsSideBar(OnAdjustmentChange onAdjustmentChange){
        this.onAdjustmentChange = onAdjustmentChange;
    }

    @Override
    protected void addElements() {
        Label title = new Label("Adjustments");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0, 0, 0, 10));

        Label brightnessLabel = new Label("Brightness:");
        Slider brightnessSlider = createSlider(-1, 1, 0);
        brightnessSlider.setOnMouseReleased(e -> triggerChange());

        Label contrastLabel = new Label("Contrast:");
        Slider contrastSlider = createSlider(-1, 1, 0);
        contrastSlider.setOnMouseReleased(e -> triggerChange());

        Label saturationLabel = new Label("Saturation:");
        Slider saturationSlider = createSlider(-3, 2, 0);
        saturationSlider.setOnMouseReleased(e -> triggerChange());

        // Store reference to sliders if needed later
        brightnessSlider.setId("brightness");
        contrastSlider.setId("contrast");
        saturationSlider.setId("saturation");

        this.getChildren().addAll(
                title, separator,
                brightnessLabel, brightnessSlider,
                contrastLabel, contrastSlider,
                saturationLabel, saturationSlider
        );
    }

    private Slider createSlider(double min, double max, double value) {
        Slider slider = new Slider(min, max, value);
        slider.setMajorTickUnit(0.5);
        slider.setBlockIncrement(0.1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        return slider;
    }

    private void triggerChange() {
        if (onAdjustmentChange != null) {
            ColorAdjust colorAdjust = new ColorAdjust();

            Slider brightness = (Slider) this.lookup("#brightness");
            Slider contrast   = (Slider) this.lookup("#contrast");
            Slider saturation = (Slider) this.lookup("#saturation");

            colorAdjust.setBrightness(brightness.getValue());
            colorAdjust.setContrast(contrast.getValue());
            colorAdjust.setSaturation(saturation.getValue());

            onAdjustmentChange.onChange(colorAdjust);
        }
    }

    public void setOnAdjustmentChange(OnAdjustmentChange listener) {
        this.onAdjustmentChange = listener;
    }
}
