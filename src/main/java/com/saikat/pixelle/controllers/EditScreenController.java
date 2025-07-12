package com.saikat.pixelle.controllers;

import com.saikat.pixelle.components.*;
import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.editor.PhotoEditor;
import com.saikat.pixelle.listeners.OnActionButtonClick;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.AlertUtil;
import com.saikat.pixelle.components.CropOverlay;
import com.saikat.pixelle.utils.FileChooserUtil;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
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
    @FXML public ScrollPane imageContainerScrollPane;
    @FXML public StackPane  imageContainerStackPane;
    @FXML public HBox       header;
    @FXML public ScrollPane sideBorderpane;
    @FXML public HBox       bodyHBox;

    private ScreenManager screenManager;
    private PhotoEditor   photoEditor;
    private AppSettings   appSettings;

    private double currentScale = 1.0;
    private ActionToggleButton lastToggleButton;

    private CropOverlay cropOverlay;

    public void initialize() {
        SavableManager savableManager = SingletonFactoryUtil.getInstance(SavableManager.class);
        appSettings = (AppSettings) savableManager.getSavableClass(AppSettings.class);

        screenManager = SingletonFactoryUtil.getInstance(ScreenManager.class);
        photoEditor = new PhotoEditor(imageView);

        cropOverlay = new CropOverlay();

        System.out.println("Inside add image: " + appSettings.getSelectedImagePath());

        addButtons();
        updateUndoRedo();
        setupListeners();
        addImage(appSettings.getSelectedImagePath());

        imageView.fitWidthProperty().bind(imageContainerScrollPane.widthProperty().divide(UI_SCALE_FACTOR));
        imageView.fitHeightProperty().bind(imageContainerScrollPane.heightProperty().divide(UI_SCALE_FACTOR));

        imageView.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds t1) {
                // imageView.setClip(clipRect);
                imageContainerStackPane.setMaxSize(t1.getWidth(), t1.getHeight());
            }
        });

        // adding sidebar
        hideSidebar();
        try {
            showRotateOverlay(imageContainerStackPane);
        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
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

        if (isToggle) {
            ActionToggleButton srcToggle = (ActionToggleButton) src;
            if ( lastToggleButton != null && !lastToggleButton.equals(srcToggle)) {
                lastToggleButton.setSelected(false);
                if ( cropOverlay != null ) cropOverlay.removeOverlay(imageContainerStackPane);
            }
            lastToggleButton = srcToggle;
            boolean selected = srcToggle.isSelected();

            switch (actionType){
                case CROP -> {
                    if (selected) showCropOverlay();
                    else cropOverlay.removeOverlay(imageContainerStackPane);
                    hideSidebar();
                }
                case BORDER -> {
                    if ( selected ) showSidebar(new BorderSideBar());
                    else hideSidebar();
                }
                case RESIZE ->{
                    if ( selected ) showSidebar(new ResizeSideBar(200, 200));
                    else hideSidebar();
                }
                case BLUR -> {
                    if ( selected ) showSidebar(new BlurPreviewSideBar(imageView));
                    else hideSidebar();
                }
                case FILTER -> {
                    if ( selected ) showSidebar(new ColorEffectPreviewSideBar(imageView));
                    else hideSidebar();
                }
                case ADJUST -> {
                    if ( selected ) showSidebar(new AdjustmentsSideBar());
                    else hideSidebar();
                }
                case TEXT -> {
                    if (selected) showSidebar(new TextSideBar());
                    else hideSidebar();
                }
            }

        } else {
            switch (actionType){
                case ZOOM_IN -> zoomIn();
                case ZOOM_OUT -> zoomOut();
                case FILE -> fileOptions(src);
                case DRAW -> screenManager.drawScreen();
                case CANCEL -> confirmAndBackToHome();
            }
        }
    }

    private void confirmAndBackToHome() {
        if (AlertUtil.confirm("Sure to cancel and go back?")){
            screenManager.entryScreen();
        };
    }

    private void showSidebar(SideBar sidebar){
        sideBorderpane.setVisible(true);
        sideBorderpane.setManaged(true);
        sideBorderpane.setContent(sidebar);
    }

    private void addImage(String path){
        Image img = new Image("file:" + path); // "file:" + "/home/saikat/Pictures/Screenshots/_.png");
        imageView.setImage(img);
        System.out.println("Image set: " + path);
    }

    private void hideSidebar(){
        sideBorderpane.setContent(null);
        sideBorderpane.setVisible(false);
        sideBorderpane.setManaged(false);
    }

    private void showCropOverlay(){
        cropOverlay.createCropper(imageContainerStackPane);
    }

    private void fileOptions(ActionButton btn) {
        ContextMenu menu = new ContextMenu();
        menu.setStyle("-fx-background-color: black; -fx-padding:5;");

        MenuItem item1 = new MenuItem("Open Image");
        MenuItem item2 = new MenuItem("Save");
        MenuItem item3 = new MenuItem("Save As");

        item1.setOnAction(e -> {
            File imageFile = FileChooserUtil.selectAnImage();
            if ( imageFile == null ){
                System.out.println("Selected image is null.");
            } else {
                imageView.setImage(new Image(imageFile.toURI().toString()));
            }
        });
        item2.setOnAction(e -> {
            System.out.println("Option 2 clicked");
            saveStackPaneToDevice();
        });
        item3.setOnAction(e -> System.out.println("Option 3 clicked"));

        menu.getItems().addAll(item1, item2, item3);

        menu.show(btn, Side.BOTTOM, 0, 0);
    }

    private void saveStackPaneToDevice(){
        File dest = FileChooserUtil.getSaveFile("filename.png");
        if ( dest == null  ) {
            System.out.println("Destination is null, could not save. | in EditScreen controller saveToDevice method.");
            return;
        }
        WritableImage snapshot = new WritableImage((int) imageContainerStackPane.getWidth(), (int) imageContainerStackPane.getHeight());
        imageContainerStackPane.snapshot(result -> {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(result.getImage(), null), "png", dest);
                System.out.println("Saved: " + dest.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }, null, snapshot);

    }

    private List<ActionButton> getListOfButtons() {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(new ActionButton("File", "fas-file", ActionType.FILE));
        buttons.add(new ActionToggleButton("Crop", "fas-crop", ActionType.CROP));
        buttons.add(new ActionToggleButton("Rotate", "fas-sync-alt", ActionType.ROTATE));
        buttons.add(new ActionToggleButton("Resize", "fas-expand-arrows-alt", ActionType.RESIZE));
        buttons.add(new ActionToggleButton("Blur", "fas-expand-arrows-alt", ActionType.BLUR));
        buttons.add(new ActionToggleButton("Border", "fas-expand-arrows-alt", ActionType.BORDER));
        buttons.add(new ActionToggleButton("Adjust", "fas-sliders-h", ActionType.ADJUST));
        buttons.add(new ActionToggleButton("Filters", "fas-magic", ActionType.FILTER));
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

        imageContainerScrollPane.setOnScroll(event -> {
            if (event.isControlDown()) {
                handleZoomEvent(event);
            } else {
                System.out.println("Regular Scroll Event: DeltaX=" + event.getDeltaX() + ", DeltaY=" + event.getDeltaY());
            }
        });

        imageContainerScrollPane.setOnKeyPressed(this::handleKeyEvent);
        imageContainerScrollPane.setOnKeyReleased(this::handleKeyEvent);
        imageContainerScrollPane.setFocusTraversable(true);

        // TODO: Not working...
        imageContainerScrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
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
            imageContainerScrollPane.setHvalue(0.5);
            imageContainerScrollPane.setVvalue(0.5);
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

    public void showRotateOverlay(StackPane imageContainerStackPane) {
        // Create dotted border rectangle
        Rectangle border = new Rectangle(0, 0);
        border.setId("rotateOverlay");
        border.setFill(null);
        border.setStroke(Color.GREEN);
        border.setStrokeWidth(3.0);
        border.getStrokeDashArray().addAll(5.0, 5.0);

        // border.widthProperty().bind(imageView.fitWidthProperty());
        // border.heightProperty().bind(imageView.fitHeightProperty());

        // Add border to StackPane
        imageContainerStackPane.getChildren().add(border);
    }
}