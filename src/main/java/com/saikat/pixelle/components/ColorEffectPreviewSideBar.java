package com.saikat.pixelle.components;

import com.saikat.pixelle.listeners.OnEffectSelected;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ColorEffectPreviewSideBar extends SideBar {
    private final ImageView originalImage;
    private OnEffectSelected<Effect> onEffectSelected;

    public ColorEffectPreviewSideBar(ImageView image) {
        this.originalImage = image;
        super();
        this.setSpacing(8.0);
    }

    public ColorEffectPreviewSideBar(ImageView imageView, OnEffectSelected<Effect> onEffectSelected) {
        this.onEffectSelected = onEffectSelected;
        this(imageView);
    }

    @Override
    protected void addElements() {
        Label title = new Label("Color Effects");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separatorTop = new Separator(Orientation.HORIZONTAL);
        VBox.setMargin(separatorTop, new Insets(0, 0, 5, 0));

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);

        // Effects
        EffectItem[] effects = {
            new EffectItem("Normal",        new ColorAdjust(0.0, 0.0, 0.0, 0.0)),
            new EffectItem("Sepia",         new SepiaTone(0.8)),
            new EffectItem("Vintage",       new ColorAdjust(-0.2, -0.3, -0.2, 0.2)),
            new EffectItem("Warm",          new ColorAdjust(0.1, 0.2, 0.1, 0.1)),
            new EffectItem("Cool",          new ColorAdjust(-0.1, -0.2, -0.1, 0.0)),
            new EffectItem("Invert",        new ColorAdjust(0.0, -1.0, 0.0, 0.0)),
            new EffectItem("Polaroid",      new ColorAdjust(0.2, 0.1, 0.3, -0.2)),
            new EffectItem("Duotone",       new ColorAdjust(0.0, 0.0, 0.0, -0.8)),
            new EffectItem("Lomo",          new ColorAdjust(0.3, 0.5, -0.2, 0.5)),
            new EffectItem("Kodachrome",    new ColorAdjust(0.1, 0.1, 0.2, 0.3)),
            // new EffectItem("BlackAndWhite", new ColorAdjust(0.0, 0.0, 0.0, -1.0)),
            new EffectItem("HighContrast",  new ColorAdjust(0.0, 0.5, 0.0, 0.0)),
            new EffectItem("Faded",         new ColorAdjust(-0.3, -0.4, 0.0, -0.5)),
            new EffectItem("Vivid",         new ColorAdjust(0.2, 0.3, 0.0, 0.4)),
            new EffectItem("SoftGlow",      new ColorAdjust(0.1, -0.2, 0.0, 0.0))
        };

        int col = 0, row = 0;
        for (EffectItem effectItem : effects) {
            ImageView preview = new ImageView(originalImage.getImage());
            preview.setFitWidth(110);
            preview.setPreserveRatio(true);
            preview.setEffect(effectItem.effect);

            StackPane container = new StackPane(preview);
            container.setMinWidth(110);
            container.getStyleClass().add("blur-preview");

            container.setOnMouseClicked( e -> {
                if ( onEffectSelected != null && effectItem.effect != null ) onEffectSelected.onSelect(effectItem.effect);
            });

            grid.add(container, col, row);

            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

        Separator separatorBottom = new Separator(Orientation.HORIZONTAL);

        this.getChildren().addAll(title, separatorTop, grid, separatorBottom);
    }

    private static class EffectItem {
        String name;
        Effect effect;

        EffectItem(String name, Effect effect) {
            this.name = name;
            this.effect = effect;
        }
    }
}
