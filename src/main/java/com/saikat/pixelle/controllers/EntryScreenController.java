package com.saikat.pixelle.controllers;

import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.HoverUtil;
import com.saikat.pixelle.utils.OpenUtil;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static com.saikat.pixelle.constants.ConstValues.*;

public class EntryScreenController {

    private ScreenManager  screenManager;
    private AppSettings appSettings;


    @FXML public VBox buttonsContainer;
    @FXML public Label titleText;

    public void initialize(){
        screenManager = SingletonFactoryUtil.getInstance(ScreenManager.class);
        SavableManager savableManager = SingletonFactoryUtil.getInstance(SavableManager.class);
        appSettings = (AppSettings) savableManager.getSavableClass(AppSettings.class);

        HoverUtil.applyScaleHover(titleText, 1.25);
    }

    @FXML
    public void checkButtonClick(Event event){
        System.out.println("Check button clicked");
    }

    @FXML
    public void drawButtonClick(MouseEvent mouseEvent) {
        showPopup("Draw On Canvas", "This screen has not been implemented");
    }

    @FXML
    public void editButtonClick(MouseEvent mouseEvent) {
        File selectedFile = openFileChooser();
        try {
            if (selectedFile != null) {
                appSettings.setLastOpenedDirPath(selectedFile.getParent());
                File destFile = new File(BASE_DIR, CURRENTLY_EDITING_IMAGE + ".png");
                Files.copy(selectedFile.toPath(), destFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                screenManager.editScreen();
            } else {
                // showPopup("No file chosen", "Please select a file to continue");
                System.out.println("No file choosen.");
            }
        } catch (IOException ex ){
            showPopup("Error opening file", ex.getMessage());
        }
    }

    @FXML
    public void onExitButtonClick(MouseEvent mouseEvent) {
        screenManager.exitApp();
    }

    @FXML
    public void onTextToImageClick(MouseEvent mouseEvent) {
        screenManager.textToImageScreen();
    }

    @FXML
    public void openHelpLink(Event event){
        OpenUtil open = SingletonFactoryUtil.getInstance(OpenUtil.class);
        open.openBrowser(STARTING_HELP_URL);
    }

    private File openFileChooser(){

        String savedPath = appSettings.getLastOpenedDirPath();
        System.out.println("Saved path: " + savedPath);
        File openingDir = savedPath != null ?
                new File(appSettings.getLastOpenedDirPath()) : new File(System.getProperty("user.home"));
        System.out.println("Opening directory: " + openingDir.getAbsolutePath());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(openingDir);
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("No file selected");
        }

        return selectedFile;
    }


    private void showPopup(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setResizable(false);
        alert.showAndWait();
    }
}
