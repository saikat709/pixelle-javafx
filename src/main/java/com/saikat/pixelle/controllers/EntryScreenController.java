package com.saikat.pixelle.controllers;

import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.Open;
import com.saikat.pixelle.utils.SingletonFactory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

import static com.saikat.pixelle.constants.ConstValues.STARTING_HELP_URL;

public class EntryScreenController {

    private ScreenManager  screenManager;
    private AppSettings appSettings;


    @FXML
    public VBox buttonsContainer;

    public void initialize(){
        screenManager = SingletonFactory.getInstance(ScreenManager.class);
        SavableManager savableManager = SingletonFactory.getInstance(SavableManager.class);
        appSettings = (AppSettings) savableManager.getSavableClass(AppSettings.class);
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
        if (selectedFile != null) {
            appSettings.setLastOpenedDirPath(selectedFile.getParent());
            screenManager.editScreen();
        } else {
            showPopup("No file chosen", "Please select a file to continue");
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
        Open open = SingletonFactory.getInstance(Open.class);
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
