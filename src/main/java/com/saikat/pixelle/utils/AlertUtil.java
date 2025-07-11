package com.saikat.pixelle.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertUtil {
    public static boolean confirm(String message) {
        final boolean[] result = {false};

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Confirmation");

        Label label = new Label(message);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Button yesBtn = new Button("Yes");
        Button noBtn = new Button("No");

        yesBtn.setOnAction(e -> {
            result[0] = true;
            dialog.close();
        });

        noBtn.setOnAction(e -> {
            result[0] = false;
            dialog.close();
        });

        HBox buttons = new HBox(10, yesBtn, noBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, label, buttons);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();

        return result[0];
    }

}
