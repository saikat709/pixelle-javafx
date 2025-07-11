package com.saikat.pixelle.utils;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class HoverUtil {

    public static void applySimpleHover(Node node) {
        node.setOnMouseEntered(e -> node.setStyle("-fx-background-color: #e0e0e0;"));
        node.setOnMouseExited(e -> node.setStyle(""));
    }

    public static void applyGlowHover(Node node) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.LIGHTBLUE);
        shadow.setRadius(10);

        node.setOnMouseEntered(e -> node.setEffect(shadow));
        node.setOnMouseExited(e -> node.setEffect(null));
    }

    public static void applyScaleHover(Node node, double scale) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(150), node);
        scaleIn.setToX(scale);
        scaleIn.setToY(scale);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(150), node);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        node.setOnMouseEntered(e -> scaleIn.playFromStart());
        node.setOnMouseExited(e -> scaleOut.playFromStart());
    }

    public static void applyComboHover(Node node) {
        DropShadow shadow = new DropShadow(10, Color.GRAY);

        node.setOnMouseEntered((MouseEvent e) -> {
            node.setStyle("-fx-background-color: #f0f0f0; -fx-cursor: hand;");
            node.setEffect(shadow);
        });

        node.setOnMouseExited((MouseEvent e) -> {
            node.setStyle("");
            node.setEffect(null);
        });
    }
}
