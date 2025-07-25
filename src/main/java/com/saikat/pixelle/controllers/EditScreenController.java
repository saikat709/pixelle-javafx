package com.saikat.pixelle.controllers;

import com.saikat.pixelle.components.*;
import com.saikat.pixelle.constants.ActionType;
import com.saikat.pixelle.editor.PhotoEditor;
import com.saikat.pixelle.listeners.*;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.AlertUtil;
import com.saikat.pixelle.components.CropOverlay;
import com.saikat.pixelle.utils.FileChooserUtil;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.saikat.pixelle.constants.ConstValues.*;

public class EditScreenController {

    @FXML public ImageView  imageView;
    @FXML public VBox       mainVerticalBox;
    @FXML public HBox       actionsBottomBar;
    @FXML public FontIcon   undoIcon, redoIcon;
    @FXML public ScrollPane bottomButtonsScrollPane;
    @FXML public ScrollPane imageContainerScrollPane;
    @FXML public StackPane  imageContainerStackPane;
    @FXML public HBox       header;
    @FXML public ScrollPane sideBorderpane;
    @FXML public HBox       bodyHBox;
    @FXML public Pane       drawTextPane;

    private ScreenManager screenManager;
    private PhotoEditor   photoEditor;
    private AppSettings   appSettings;

    private double  currentScale = 1.0;
    private int     currentRotate = 0;
    private boolean isSidebarVisible   = false;
    private boolean adjustmentsChanges = false;
    private ActionToggleButton lastToggleButton;

    private CropOverlay      cropOverlay;
    private Label            label;
    private Rectangle        imageViewBoundingRect;
    private BorderRectangle  currentBorderRect;
    private GaussianBlur     gaussianBlur;
    private Effect           colorEffect;
    private ColorAdjust      colorAdjust;

    private AdjustmentsSideBar adjustmentsSideBar;
    private BorderSideBar      borderSideBar;

    public void initialize() {
        SavableManager savableManager = SingletonFactoryUtil.getInstance(SavableManager.class);
        this.appSettings = (AppSettings) savableManager.getSavableClass(AppSettings.class);

        this.imageViewBoundingRect = new Rectangle();
        this.screenManager         = SingletonFactoryUtil.getInstance(ScreenManager.class);
        this.photoEditor           = new PhotoEditor(imageView, imageContainerStackPane, drawTextPane);
        this.cropOverlay           = new CropOverlay();

        this.currentBorderRect  = new BorderRectangle();
        this.gaussianBlur       = null;
        this.colorEffect        = null;
        this.colorAdjust        = null;

        addButtons();
        setupListeners();
        addImage(appSettings.getSelectedImagePath());

        // Bind StackPane size to ScrollPane viewport size
        imageContainerStackPane.prefWidthProperty().bind(imageContainerScrollPane.widthProperty());
        imageContainerStackPane.prefHeightProperty().bind(imageContainerScrollPane.heightProperty());

        // Listener for ScrollPane size changes
        imageContainerScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            resizeToFit(imageView, newVal.doubleValue(), imageContainerScrollPane.getHeight());
        });
        imageContainerScrollPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            resizeToFit(imageView, imageContainerScrollPane.getWidth(), newVal.doubleValue());
        });

        // Listener for ImageView bounds changes
        this.imageView.boundsInLocalProperty().addListener((observableValue, bounds, newBounds) -> {
            System.out.println("imageview size changed. " + newBounds + ", " + bounds);
            resizeToFit(imageView, imageContainerStackPane.getWidth(), imageContainerStackPane.getHeight());
        });

        this.adjustmentsSideBar = new AdjustmentsSideBar(adjustments -> {
            this.colorAdjust = adjustments;
            imageView.setEffect(photoEditor.getappliedEffect(adjustments));
            adjustmentsChanges = true;
        });

        this.borderSideBar = new BorderSideBar((border, width) -> {
            try {
                currentBorderRect.setStroke(border);
                currentBorderRect.setStrokeWidth(width);
                currentBorderRect.setFill(Color.TRANSPARENT);

                currentBorderRect.setWidth(imageViewBoundingRect.getWidth() - width + 5);
                currentBorderRect.setHeight(imageViewBoundingRect.getHeight() - width + 5);

                if (imageContainerStackPane.getChildren().contains(photoEditor.getLastborderRectangle()))
                    imageContainerStackPane.getChildren().remove(photoEditor.getLastborderRectangle());

                photoEditor.addBorder(currentBorderRect);
                updateRedoUndoState();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        hideSidebar();

        redoIcon.setOnMouseClicked(event -> {
            photoEditor.redo();
            updateRedoUndoState();
        });

        undoIcon.setOnMouseClicked(event -> {
            photoEditor.undo();
            updateRedoUndoState();
        });

        updateRedoUndoState();
    }

    private void updateRedoUndoState() {
        redoIcon.setDisable(!photoEditor.canRedo());
        undoIcon.setDisable(!photoEditor.canUndo());
    }

    private void addButtons() {
        actionsBottomBar.getChildren().clear();

        List<ActionButton> buttons = getListOfButtons();
        for (ActionButton button : buttons) {
            button.setOnActionButtonClick((event, actionType) -> {
                ActionButton src = (ActionButton) event.getSource();
                handleActionButtonClick(actionType, src);
            });
            actionsBottomBar.getChildren().add(button);
        }
    }

    private void handleActionButtonClick(ActionType actionType, ActionButton src) {
        boolean isToggle = src instanceof ActionToggleButton;

        if (isToggle) {
            ActionToggleButton srcToggle = (ActionToggleButton) src;
            if (lastToggleButton != null && !lastToggleButton.equals(srcToggle)) {
                lastToggleButton.setSelected(false);
                if (cropOverlay != null) cropOverlay.removeOverlay(imageContainerStackPane);
            }
            lastToggleButton = srcToggle;
            boolean selected = srcToggle.isSelected();

            switch (actionType) {
                case CROP -> {
                    if (selected) showCropOverlay();
                    else cropOverlay.removeOverlay(imageContainerStackPane);
                    hideSidebar();
                }
                case BORDER -> {
                    if (selected) showSidebar(this.borderSideBar);
                    else hideSidebar();
                }
                case RESIZE -> {
                    if (selected) showSidebar(new ResizeSideBar(200, 200));
                    else hideSidebar();
                }
                case BLUR -> {
                    if (selected) {
                        showSidebar(new BlurPreviewSideBar(getOriginalImageView(), effect -> {
                            this.gaussianBlur = effect;
                            imageView.setEffect(photoEditor.getappliedEffect(effect));
                        }));
                    } else {
                        hideSidebar();
                    }
                }
                case FILTER -> {
                    if (selected) {
                        showSidebar(new ColorEffectPreviewSideBar(getOriginalImageView(), effect -> {
                            this.colorEffect = effect;
                            imageView.setEffect(photoEditor.getappliedEffect(effect));
                        }));
                    } else {
                        hideSidebar();
                    }
                }
                case ADJUST -> {
                    if (selected) {
                        showSidebar(this.adjustmentsSideBar);
                    } else {
                        hideSidebar();
                    }
                }
                case TEXT -> {
                    if (selected) showTextSideBar();
                    else hideSidebar();
                }
                case DRAW -> {
                    if (selected) showSidebar(new DrawSidebar(drawTextPane, finalShape -> {
                        photoEditor.addDrawCommand(finalShape);
                        updateRedoUndoState();
                    }));
                    else hideSidebar();
                }
            }
        } else {
            switch (actionType) {
                case ZOOM_IN -> zoomIn();
                case ZOOM_OUT -> zoomOut();
                case FILE -> fileOptions(src);
                case ROTATE -> rotateImageTo90Degree();
                case CANCEL -> confirmAndBackToHome();
            }
        }

        updateRedoUndoState();
    }

    private ImageView getOriginalImageView() {
        return new ImageView(new Image("file:" + appSettings.getSelectedImagePath()));
    }

    private void showTextSideBar() {
        label = new Label("EDIT TEXT");
        showTextSideBar(label);
    }

    private void showTextSideBar(Label label) {
        showSidebar(new TextSideBar(label, new OnTextEditorEvent() {
            @Override
            public void onAddAndClose(Label edited) {
                super.onAddAndClose(edited);
                hideSidebar();
            }

            @Override
            public void onAddAndNext(Label edited) {
                super.onAddAndNext(edited);
            }

            @Override
            public void onDelete(Label label1) {
                System.out.println("Delete button click.");
            }
        }));
        imageContainerStackPane.getChildren().add(label);
    }

    private void addImage(String path) {
        Image img = new Image("file:" + path);
        imageView.setImage(img);
        photoEditor.initializeImageSize(img);
        System.out.println("Image set: " + path);
        resizeToFit(imageView, imageContainerScrollPane.getWidth(), imageContainerScrollPane.getHeight());
    }

    private void confirmAndBackToHome() {
        if (AlertUtil.confirm("Sure to cancel and go back?")) {
            screenManager.entryScreen();
        }
    }

    private void showSidebar(SideBar sidebar) {
        if (this.isSidebarVisible) {
            checkEditToAdd();
        }

        sideBorderpane.setVisible(true);
        sideBorderpane.setManaged(true);
        sideBorderpane.setContent(sidebar);

        this.isSidebarVisible = true;
    }

    private void checkEditToAdd() {
        if (gaussianBlur != null) {
            photoEditor.addBlurEffect(gaussianBlur);
            gaussianBlur = null;
        }

        if (colorEffect != null) {
            photoEditor.addColorEffect(colorEffect);
            colorEffect = null;
        }

        if (adjustmentsChanges) {
            photoEditor.addColorEffect(colorAdjust);
            colorAdjust = null;
            adjustmentsChanges = false;
        }

        this.imageView.setEffect(photoEditor.getLastEffect());
    }

    private void hideSidebar() {
        checkEditToAdd();

        sideBorderpane.setContent(null);
        sideBorderpane.setVisible(false);
        sideBorderpane.setManaged(false);

        if (label != null) {
            label = null;
        }

        this.isSidebarVisible = false;
    }

    private void showCropOverlay() {
        cropOverlay.createCropper(imageContainerStackPane, imageViewBoundingRect);
    }

    private void fileOptions(ActionButton btn) {
        ContextMenu menu = new ContextMenu();
        menu.setStyle("-fx-background-color: black; -fx-padding:5;");

        MenuItem item1 = new MenuItem("Open Image");
        MenuItem item2 = new MenuItem("Save");
        MenuItem item3 = new MenuItem("Save As");

        item1.setOnAction(e -> {
            File imageFile = FileChooserUtil.selectAnImage();
            if (imageFile == null) {
                System.out.println("Selected image is null.");
            } else {
                imageView.setImage(new Image(imageFile.toURI().toString()));
                resizeToFit(imageView, imageContainerScrollPane.getWidth(), imageContainerScrollPane.getHeight());
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

    private void saveStackPaneToDevice() {
        File dest = FileChooserUtil.getSaveFile("filename.png");
        if (dest == null) {
            System.out.println("Destination is null, could not save. | in EditScreen controller saveToDevice method.");
            return;
        }
        photoEditor.exportToPng(dest);
    }

    private List<ActionButton> getListOfButtons() {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(new ActionButton("File", "fas-file", ActionType.FILE));
        buttons.add(new ActionToggleButton("Crop", "fas-crop", ActionType.CROP));
        buttons.add(new ActionButton("Rotate", "fas-sync-alt", ActionType.ROTATE));
        buttons.add(new ActionToggleButton("Resize", "fas-expand-arrows-alt", ActionType.RESIZE));
        buttons.add(new ActionToggleButton("Blur", "fas-expand-arrows-alt", ActionType.BLUR));
        buttons.add(new ActionToggleButton("Border", "fas-expand-arrows-alt", ActionType.BORDER));
        buttons.add(new ActionToggleButton("Adjust", "fas-sliders-h", ActionType.ADJUST));
        buttons.add(new ActionToggleButton("Filters", "fas-magic", ActionType.FILTER));
        buttons.add(new ActionButton("Zoom In", "fas-search-plus", ActionType.ZOOM_IN));
        buttons.add(new ActionButton("Zoom Out", "fas-search-minus", ActionType.ZOOM_OUT));
        buttons.add(new ActionToggleButton("Draw", "fas-pen", ActionType.DRAW));
        buttons.add(new ActionToggleButton("Text", "fas-font", ActionType.TEXT));
        buttons.add(new ActionButton("Reset", "fas-undo-alt", ActionType.RESET));
        buttons.add(new ActionButton("Cancel", "fas-times", ActionType.CANCEL));
        return buttons;
    }

    private void setupListeners() {
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
    }

    @FXML
    public void onBackIconClick(MouseEvent mouseEvent) {
        screenManager.entryScreen();
    }

    private void handleKeyEvent(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.EQUALS && event.getEventType() == KeyEvent.KEY_PRESSED) {
            System.out.println("Ctrl + Plus (KP_ADD) key combination detected for zoom IN.");
            event.consume();
            zoomIn();
        } else if (event.isControlDown() && event.getCode() == KeyCode.MINUS && event.getEventType() == KeyEvent.KEY_PRESSED) {
            System.out.println("Ctrl + Minus (KP_SUBTRACT) key combination detected for zoom OUT.");
            event.consume();
            zoomOut();
        } else if (event.isControlDown() && event.getCode() == KeyCode.DIGIT0 && event.getEventType() == KeyEvent.KEY_PRESSED) {
            System.out.println("Ctrl + 0 (DIGIT0) key combination detected for reset zoom.");
            event.consume();
            currentScale = 1.0;
            imageView.setScaleX(currentScale);
            imageView.setScaleY(currentScale);
            imageContainerScrollPane.setHvalue(0.5);
            imageContainerScrollPane.setVvalue(0.5);
            resizeToFit(imageView, imageContainerScrollPane.getWidth(), imageContainerScrollPane.getHeight());
        }
    }

    private void handleZoomEvent(ScrollEvent event) {
        event.consume();
        if (event.getDeltaY() == 0) return;

        double zoomFactor = (event.getDeltaY() > 0) ? ZOOM_FACTOR : 1 / ZOOM_FACTOR;
        String zoomType = event.getEventType() == ScrollEvent.SCROLL ? "Mouse Wheel Zoom" : "Touchpad Zoom";
        System.out.println(zoomType + " detected. Zoom Factor: " + zoomFactor);

        currentScale = Math.max(Math.min(currentScale * zoomFactor, 1.85), 0.35);
        resizeToFit(imageView, imageContainerStackPane.getWidth(), imageContainerStackPane.getHeight());
    }

    private void zoomIn() {
        currentScale = Math.max(Math.min(currentScale * ZOOM_FACTOR, 1.85), 0.35);
        resizeToFit(imageView, imageContainerStackPane.getWidth(), imageContainerStackPane.getHeight());
    }

    private void zoomOut() {
        currentScale = Math.max(Math.min(currentScale / ZOOM_FACTOR, 1.85), 0.35);
        resizeToFit(imageView, imageContainerStackPane.getWidth(), imageContainerStackPane.getHeight());
    }

    public void rotateImageTo90Degree() {
        currentRotate += 90;
        currentRotate = currentRotate % 360;
        imageView.setRotate(currentRotate);
        resizeToFit(imageView, imageContainerScrollPane.getWidth(), imageContainerScrollPane.getHeight());
    }

    private void resizeToFit(ImageView imageView, double paneWidth, double paneHeight) {
        if (imageView.getImage() == null) return;

        double angle = Math.toRadians(imageView.getRotate());
        double cos   = Math.abs(Math.cos(angle));
        double sin   = Math.abs(Math.sin(angle));

        double imageWidth  = imageView.getImage().getWidth();
        double imageHeight = imageView.getImage().getHeight();

        // Calculate rotated bounding box dimensions
        double rotatedWidth  = imageWidth * cos + imageHeight * sin;
        double rotatedHeight = imageWidth * sin + imageHeight * cos;

        // Calculate scale to fit within the parent StackPane
        double scale = Math.min(paneWidth / rotatedWidth, paneHeight / rotatedHeight);
        scale *= currentScale; // Incorporate zoom factor

        // Apply scaling
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);

        // Ensure the image is centered
         imageView.setTranslateX( - rotatedWidth * scale / 4 );
         imageView.setTranslateY( - rotatedHeight * scale / 2 );

        // Update the bounding rectangle for other components (e.g., crop overlay, border)
        imageViewBoundingRect.setWidth(rotatedWidth * scale);
        imageViewBoundingRect.setHeight(rotatedHeight * scale);
    }
}