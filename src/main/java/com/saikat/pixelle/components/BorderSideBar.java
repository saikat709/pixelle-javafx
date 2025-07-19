package com.saikat.pixelle.components;

import com.saikat.pixelle.listeners.OnBorderUpdate;
import com.saikat.pixelle.listeners.OnColorSelect;
import com.saikat.pixelle.listeners.OnSliderChange;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.event.MouseEvent;

public class BorderSideBar extends SideBar {

    private OnBorderUpdate onBorderUpdate;
    private Color  selectedBorderColor;
    private Double selectedBorderWidth;

    public BorderSideBar(){
        this(null);
    }

    public  BorderSideBar(OnBorderUpdate onBorderUpdate){
        this.onBorderUpdate      = onBorderUpdate;
        this.selectedBorderColor = Color.WHITE;
        this.selectedBorderWidth = 0.0;
        super();
    }

    @Override
    protected void addElements(){
        OnBorderUpdate onBorderUpdate1 = this.onBorderUpdate;

        Label title = new Label("Image Border");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(0,0,0,10));

        HBox colorSelection = new HBox(10);
        Label colorLabel = new Label("Pick Color: ");
        colorLabel.setFont(Font.font("Arial", 16));
        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);
        ColorPicker colorPicker = getColorPicker();
        colorSelection.getChildren().addAll(colorLabel, space, colorPicker);

        colorPicker.setOnAction(event -> {
            this.selectedBorderColor = colorPicker.getValue();
            if ( onBorderUpdate1 != null ) onBorderUpdate1.onBorderUpdate(selectedBorderColor, selectedBorderWidth);
        });

        Label width = new Label("Width: ");
        width.setFont(Font.font("Arial", 16));
        Slider slider = new Slider(0, 20, 0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        Button deleteButton = new Button("DELETE");
        deleteButton.getStyleClass().addAll("btn", "secondary");

        deleteButton.setOnAction(event -> {
            this.selectedBorderWidth = 0.0;
            this.selectedBorderColor = Color.TRANSPARENT;
            if ( onBorderUpdate1 != null ) onBorderUpdate1.onBorderUpdate(Color.TRANSPARENT, 0.0);
        });

        slider.setOnMouseReleased(event -> {
            this.selectedBorderWidth = slider.getValue();
            this.selectedBorderColor = colorPicker.getValue();
            if ( onBorderUpdate1 != null ) onBorderUpdate1.onBorderUpdate(selectedBorderColor, selectedBorderWidth);
        });

        this.getChildren().addAll(title, separator, colorSelection, width, slider, deleteButton);
    }

    private ColorPicker getColorPicker() {
        ColorPicker colorPicker = new ColorPicker();

        OnBorderUpdate listener = this.onBorderUpdate;
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Color c = colorPicker.getValue();
                System.out.println(c.toString());
                if ( listener != null ) listener.onBorderUpdate(selectedBorderColor, selectedBorderWidth);
            }
        });
        return colorPicker;
    }
}
