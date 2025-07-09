package com.saikat.pixelle.controllers;

import com.saikat.pixelle.components.ActionButton;
import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.editor.Gray;
import com.saikat.pixelle.editor.PhotoEditor;
import com.saikat.pixelle.listeners.OnActionButtonClick;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.stage.Stage;

import java.io.File;

import static com.saikat.pixelle.constants.ConstValues.*;

public class EditScreenController {

    @FXML public ImageView imageView;
    @FXML public VBox       mainVerticalBox;
    @FXML public HBox       actionsBottomBar;
    @FXML public FontIcon   undoIcon;

    @FXML public FontIcon   redoIcon;
    @FXML public ScrollPane bottomButtonsScrollPane;
    @FXML public ScrollPane imageContainer;
    @FXML public HBox header;

    private ScreenManager screenManager;
    private PhotoEditor   photoEditor;

    private double currentScale = 1.0;
    private static final double ZOOM_FACTOR = 1.05;


    public void initialize(){

        System.out.println("Initialized EditController.");

        screenManager = SingletonFactoryUtil.getInstance(ScreenManager.class);

        File file = new File(BASE_DIR, CURRENTLY_EDITING_IMAGE + ".png");
        photoEditor = new PhotoEditor(imageView);

        addButtons();
        updateUndoRedo();
        setupListeners();


        Image img = new Image("file:" + "/home/saikat/Pictures/Screenshots/_.png");
        imageView.setImage(img);

        imageView.fitWidthProperty().bind(imageContainer.widthProperty().divide(UI_SCALE_FACTOR));
        imageView.fitHeightProperty().bind(imageContainer.heightProperty().divide(UI_SCALE_FACTOR));

        imageView.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds t1) {
                // imageView.setClip(clipRect);
            }
        });
    }

    private void addButtons(){
        actionsBottomBar.getChildren().clear();
        ActionButton saveButton = new ActionButton("Save", "fas-save", ActionType.SAVE);
        saveButton.setOnActionButtonClick(new OnActionButtonClick() {
            @Override
            public void onClick(Event event, ActionType actionType) {
                imageView.setScaleY(1.5);
            }
        });

        ActionButton drawButton = new ActionButton("Draw", "fas-pen",   ActionType.DRAW);
        ActionButton exitButton = new ActionButton("Draw", "fas-cross", ActionType.EXIT);

        exitButton.setOnActionButtonClick(new OnActionButtonClick() {
            @Override
            public void onClick(Event event, ActionType actionType) {
                photoEditor.addEditingCommand(new Gray());
                updateUndoRedo();
            }
        });


        exitButton.setIconCode(FontAwesomeSolid.CROP);
        actionsBottomBar.getChildren().addAll(saveButton, drawButton, exitButton);

        for( int i = 0; i < 50; i++){
            ActionButton newBtn = new ActionButton("New", "fas-pen");
            actionsBottomBar.getChildren().add(newBtn);
        }
    }

    private void setupListeners(){

        // undo redo
        undoIcon.setOnMouseClicked(event -> {
            photoEditor.undo();
            updateUndoRedo();
        });

        redoIcon.setOnMouseClicked( event -> {
            photoEditor.redo();
            updateUndoRedo();
        });

        imageContainer.setOnScroll(event -> {
            if (event.isControlDown()) {
                handleZoomEvent(event);
            } else {
                System.out.println("Regular Scroll Event: DeltaX=" + event.getDeltaX() + ", DeltaY=" + event.getDeltaY());
            }
        });

        imageContainer.setOnKeyPressed(this::handleKeyEvent);
        imageContainer.setOnKeyReleased(this::handleKeyEvent);
        imageContainer.setFocusTraversable(true);

        // TODO: Not working...
        imageContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getFocusOwner().focusedProperty().addListener((obs2, oldVal, newVal) -> {
                    if (newVal)  imageContainer.requestFocus();
                });
            }
        });
    }

    private void updateUndoRedo() {
        System.out.println("check: undo: - " + undoIcon.isDisable() + " " + redoIcon.isDisable() );
        undoIcon.setDisable(!photoEditor.hasPrevious());
        redoIcon.setDisable(!photoEditor.hasNext());
    }

    @FXML
    public void onBackIconClick(MouseEvent mouseEvent) {
        screenManager.entryScreen();
    }

    private void handleKeyEvent(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.EQUALS && event.getEventType() == KeyEvent.KEY_PRESSED) {
            System.out.println("Ctrl + Plus (KP_ADD) key combination detected for zoom IN.");
            event.consume();
            currentScale *= ZOOM_FACTOR;
            imageView.setScaleX(currentScale);
            imageView.setScaleY(currentScale);
            System.out.println("Current Scale: " + currentScale);

        } else if (event.isControlDown() && event.getCode() == KeyCode.MINUS && event.getEventType() == KeyEvent.KEY_PRESSED) {
            System.out.println("Ctrl + Minus (KP_SUBTRACT) key combination detected for zoom OUT.");
            event.consume(); // Consume the event if you handle it

            currentScale /= ZOOM_FACTOR;
            if (currentScale < 0.1) currentScale = 0.1;
            imageView.setScaleX(currentScale);
            imageView.setScaleY(currentScale);
            System.out.println("Current Scale: " + currentScale);

        } else if (event.isControlDown() && event.getCode() == KeyCode.DIGIT0 && event.getEventType() == KeyEvent.KEY_PRESSED) {
            System.out.println("Ctrl + 0 (DIGIT0) key combination detected for reset zoom.");
            event.consume();
            currentScale = 1.0;
            imageView.setScaleX(currentScale);
            imageView.setScaleY(currentScale);
            imageContainer.setHvalue(0.5);
            imageContainer.setVvalue(0.5);
            System.out.println("Current Scale: " + currentScale + " (Reset)");
        }
    }

    private void handleZoomEvent(ScrollEvent event) {
        event.consume();

        if ( event.getDeltaY() == 0 ) return;

        double zoomFactor = (event.getDeltaY() > 0) ? ZOOM_FACTOR : 1 / ZOOM_FACTOR;
        String zoomType = event.getEventType() == ScrollEvent.SCROLL ? "Mouse Wheel Zoom" : "Touchpad Zoom";

        System.out.println(zoomType + " detected. Zoom Factor: " + zoomFactor);

        currentScale = Math.max(Math.min(currentScale * zoomFactor, 1.85), 0.35);
        imageView.setScaleX(currentScale);
        imageView.setScaleY(currentScale);
    }
}

