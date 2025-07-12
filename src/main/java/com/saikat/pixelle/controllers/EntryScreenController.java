package com.saikat.pixelle.controllers;

import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.FileChooserUtil;
import com.saikat.pixelle.utils.HoverUtil;
import com.saikat.pixelle.utils.OpenUtil;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.File;

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
        // showPopup("Draw On Canvas", "This screen has not been implemented");
        screenManager.drawScreen();
    }

    @FXML
    public void editButtonClick(MouseEvent mouseEvent) {
        File selectedFile = FileChooserUtil.selectAnImage();

        if ( selectedFile == null ){
            // showPopup("No file chosen", "Please select a file to continue");
            System.out.println("No file chosen.");
        }

        appSettings.setLastOpenedDirPath(selectedFile.getParent());
        appSettings.setSelectedImagePath(selectedFile.getAbsolutePath());
        System.out.println("Selected: " + selectedFile.getAbsolutePath());
        screenManager.editScreen();

        /**
        File destFile = new File(BASE_DIR, CURRENTLY_EDITING_IMAGE + ".png");
        Files.copy(selectedFile.toPath(),
                destFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES);
         */
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



    private void showPopup(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setResizable(false);
        alert.showAndWait();
    }
}
