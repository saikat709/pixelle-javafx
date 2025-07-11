package com.saikat.pixelle.components;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BlurPreviewSideBar extends SideBar {
    private final ImageView originalImage;

    public BlurPreviewSideBar(ImageView image) {
        this.originalImage = image;
        super();
    }

    @Override
    protected void addElements() {
        Label title = new Label("Blur Previews");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setMaxWidth(60);
        VBox.setMargin(separator, new Insets(0, 0, 0, 10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(8));

        double[] blurLevels = {2, 5, 7, 10, 12, 16};
        int col = 0, row = 0;

        for (double blurValue : blurLevels) {
            ImageView preview = new ImageView(originalImage.getImage());
            preview.setFitWidth(70);
            preview.setPreserveRatio(true);
            preview.setEffect(new GaussianBlur(blurValue));

            StackPane container = new StackPane(preview);
            container.getStyleClass().add("blur-preview");

            grid.add(container, col, row);

            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
        this.getChildren().addAll(title, separator, grid);
    }
}
