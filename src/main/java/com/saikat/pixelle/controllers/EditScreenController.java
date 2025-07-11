package com.saikat.pixelle.controllers;

import com.saikat.pixelle.components.*;
import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.editor.Gray;
import com.saikat.pixelle.editor.PhotoEditor;
import com.saikat.pixelle.listeners.OnActionButtonClick;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.utils.AlertUtil;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Effect;
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
import java.util.ArrayList;
import java.util.List;

import static com.saikat.pixelle.constants.ConstValues.*;

public class EditScreenController {

    @FXML public ImageView  imageView;
    @FXML public VBox       mainVerticalBox;
    @FXML public HBox       actionsBottomBar;
    @FXML public FontIcon   undoIcon;

    @FXML public FontIcon   redoIcon;
    @FXML public ScrollPane bottomButtonsScrollPane;
    @FXML public ScrollPane imageContainer;
    @FXML public HBox       header;
    @FXML public ScrollPane sideBorderpane;
    @FXML public HBox       bodyHBox;

    private ScreenManager screenManager;
    private PhotoEditor   photoEditor;

    private double currentScale = 1.0;
    private ActionToggleButton lastToggleButton;


    public void initialize() {

        screenManager = SingletonFactoryUtil.getInstance(ScreenManager.class);

        File file = new File(BASE_DIR, CURRENTLY_EDITING_IMAGE + ".png");
        photoEditor = new PhotoEditor(imageView);

        addButtons();
        updateUndoRedo();
        setupListeners();


        Image img = new Image("file:" + file.getAbsolutePath()); // "file:" + "/home/saikat/Pictures/Screenshots/_.png");
        imageView.setImage(img);

        imageView.fitWidthProperty().bind(imageContainer.widthProperty().divide(UI_SCALE_FACTOR));
        imageView.fitHeightProperty().bind(imageContainer.heightProperty().divide(UI_SCALE_FACTOR));

        imageView.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds t1) {
                // imageView.setClip(clipRect);
            }
        });

        // adding sidebar
        sideBorderpane.setContent(new ColorEffectPreviewSideBar(imageView));

    }

    private void addButtons(){
        actionsBottomBar.getChildren().clear();

        List<ActionButton> buttons = getListOfButtons();
        for(ActionButton button : buttons){
            button.setOnActionButtonClick(new OnActionButtonClick() {
                @Override
                public void onClick(Event event, ActionType actionType) {
                    ActionButton src = (ActionButton) event.getSource();
                    handleActionButtonClick(actionType, src );
                }
            });
            actionsBottomBar.getChildren().add(button);
        }
    }

    private void handleActionButtonClick(ActionType actionType, ActionButton src){
        boolean isToggle = src instanceof ActionToggleButton;
        System.out.println("Click: " + actionType.toString() + ", Is toggle: " + isToggle );

        if ( isToggle ) {
            // TODO: Manage
            if ( lastToggleButton != null ) lastToggleButton.setSelected(false);
            lastToggleButton = (ActionToggleButton) src;
        }

        switch (actionType){
            case ZOOM_IN -> zoomIn();
            case ZOOM_OUT -> zoomOut();
            case FILE -> fileOptions(src);
            case BLUR -> showSidebar(new BlurPreviewSideBar(imageView));
            case FILTER -> showSidebar(new ColorEffectPreviewSideBar(imageView));
            case ADJUST -> showSidebar(new AdjustmentsSideBar());
            case CANCEL -> confirmAndBackToHome();
        }

        // TODO: border option
    }

    private void confirmAndBackToHome() {
        if (AlertUtil.confirm("Sure to cancel and go back?")){
            screenManager.entryScreen();
        };
    }


    private void showSidebar(Object sidebar){
        sideBorderpane.setContent((Node) sidebar);
    }

    private void fileOptions(ActionButton btn) {
        ContextMenu menu = new ContextMenu();
        menu.setStyle("-fx-background-color: black; -fx-padding:5;");

        MenuItem item1 = new MenuItem("Open Image");
        MenuItem item2 = new MenuItem("Save");
        MenuItem item3 = new MenuItem("Save As");

        item1.setOnAction(e -> System.out.println("Option 1 clicked"));
        item2.setOnAction(e -> System.out.println("Option 2 clicked"));
        item3.setOnAction(e -> System.out.println("Option 3 clicked"));

        menu.getItems().addAll(item1, item2, item3);

        menu.show(btn, Side.BOTTOM, 0, 0);
    }

    private List<ActionButton> getListOfButtons() {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(new ActionButton("File", "fas-file", ActionType.FILE));
        buttons.add(new ActionToggleButton("Crop", "fas-crop", ActionType.CROP));
        buttons.add(new ActionToggleButton("Rotate", "fas-sync-alt", ActionType.ROTATE));
        buttons.add(new ActionToggleButton("Resize", "fas-expand-arrows-alt", ActionType.RESIZE));
        buttons.add(new ActionToggleButton("Adjust", "fas-sliders-h", ActionType.ADJUST));
        buttons.add(new ActionToggleButton("Filters", "fas-magic", ActionType.FILTER));
        buttons.add(new ActionButton("Redo", "fas-redo", ActionType.REDO));
        buttons.add(new ActionButton("Zoom In", "fas-search-plus", ActionType.ZOOM_IN));
        buttons.add(new ActionButton("Zoom Out", "fas-search-minus", ActionType.ZOOM_OUT));
        buttons.add(new ActionButton("Draw", "fas-pen", ActionType.DRAW));
        buttons.add(new ActionToggleButton("Text", "fas-font", ActionType.TEXT));
        buttons.add(new ActionButton("Reset", "fas-undo-alt", ActionType.RESET));
        buttons.add(new ActionButton("Cancel", "fas-times", ActionType.CANCEL));
        return buttons;
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
                // newScene.getFocusOwner().focusedProperty().addListener((obs2, oldVal, newVal) -> {
                //    if (newVal)  imageContainer.requestFocus();
                // });
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

    private void zoomIn(){
        currentScale = Math.max(Math.min(currentScale * ZOOM_FACTOR, 1.85), 0.35);
        imageView.setScaleX(currentScale);
        imageView.setScaleY(currentScale);
    }

    private void zoomOut(){
        currentScale = Math.max(Math.min(currentScale / ZOOM_FACTOR, 1.85), 0.35);
        imageView.setScaleX(currentScale);
        imageView.setScaleY(currentScale);
    }
}