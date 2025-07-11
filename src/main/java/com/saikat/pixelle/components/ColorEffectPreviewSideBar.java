package com.saikat.pixelle.components;

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

    public ColorEffectPreviewSideBar(ImageView image) {
        this.originalImage = image;
        super();
        this.setSpacing(8.0);
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
                new EffectItem("Normal", null),
                new EffectItem("Sepia", new SepiaTone(0.8)),
                new EffectItem("Vintage", new ColorAdjust(-0.2, -0.3, -0.2, 0.2)),
                new EffectItem("Warm", new ColorAdjust(0.1, 0.2, 0.1, 0.1)),
                new EffectItem("Cool", new ColorAdjust(-0.1, -0.2, -0.1, 0)),
                new EffectItem("Invert", null),
                new EffectItem("Polaroid", new ColorAdjust(0.2, 0.1, 0.3, -0.2)),
                new EffectItem("Duotone", null),
                new EffectItem("Lomo", new ColorAdjust(0.3, 0.5, -0.2, 0.5)),
                new EffectItem("Kodachrome", new ColorAdjust(0.1, 0.1, 0.2, 0.3)),
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
