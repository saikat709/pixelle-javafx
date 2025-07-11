package com.saikat.pixelle.components;

import com.saikat.pixelle.listeners.OnColorSelect;
import com.saikat.pixelle.listeners.OnSliderChange;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.event.MouseEvent;

public class BorderSideBar extends SideBar {

    private OnSliderChange onSliderChange;
    private OnColorSelect  onColorSelect;

    public BorderSideBar(){
        this.onSliderChange = null;
        this.onColorSelect = null;
    }

    @Override
    protected void addElements(){

        Label title = new Label("Image Border");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0,0,0,10));

        Label color = new Label("Pick Color: ");
        color.setFont(Font.font("Arial", 16));

        ColorPicker colorPicker = getColorPicker();

        Label width = new Label("Width: ");
        width.setFont(Font.font("Arial", 16));
        Slider slider = new Slider(0, 20, 5);

        OnSliderChange sliderChange = onSliderChange;
        slider.setOnMouseReleased(event -> {
            System.out.println(slider.getValue());
            if ( sliderChange != null ) sliderChange.onChange(slider.getValue());
        });

        this.getChildren().addAll(title, separator, color, colorPicker, width, slider);
    }

    private ColorPicker getColorPicker() {
        ColorPicker colorPicker = new ColorPicker();

        OnColorSelect listener = onColorSelect;
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Color c = colorPicker.getValue();
                System.out.println(c.toString());
                if ( listener != null ) listener.onSelect(c);
            }
        });
        return colorPicker;
    }
}
