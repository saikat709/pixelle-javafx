package com.saikat.pixelle.controllers;

import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.SingletonFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.*;

public class TextToImageScreenController {

    @FXML public VBox mainGridLayout;
    @FXML public Button generateButton;
    @FXML public TextField imagePrompt;
    @FXML public Label nothingGeneratedText;
    @FXML public VBox generatingImageIndicator;
    @FXML public Label errorOccurredText;
    @FXML public HBox generatedImage;

    private ProgressIndicator progressIndicator;

    private boolean isGenerating = false;
    private ScreenManager screenManager;

    public void initialize() {

        screenManager = SingletonFactory.getInstance(ScreenManager.class);
        generateButton.setDisable(true);

        imagePrompt.setOnKeyPressed( e -> {
            generateButton.setDisable(imagePrompt.getText().isEmpty());
        });

        progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(19, 19);

        generateButton.setOnAction(this::onGenerateButtonClick);

    }


    private void onGenerateButtonClick(ActionEvent event) {
        if( isGenerating && confirmCancelGeneration() ) {
            generateButton.setText("Generate");
            imagePrompt.setDisable(false);
            generateButton.setGraphic(null);
            decideVisibleContent(nothingGeneratedText.getId());
            isGenerating = false;
        } else  {
            generateButton.setText("Generating...");
            imagePrompt.setDisable(true);
            generateButton.setGraphic(progressIndicator);
            decideVisibleContent(generatingImageIndicator.getId());
            isGenerating = true;
            // generateImage();
            onImageGenerationComplete();
        }

    }


    private void generateImage(){
        try ( ScheduledExecutorService a = Executors.newSingleThreadScheduledExecutor() ) {
            a.schedule(this::onImageGenerationComplete, 5, TimeUnit.SECONDS);
        } catch ( Exception e ) {
            System.err.println(e.getMessage());
        }
    }


     private void onImageGenerationComplete(){
        decideVisibleContent(generatedImage.getId());
         generateButton.setText("Generate");
         imagePrompt.setDisable(false);
         generateButton.setGraphic(null);
         isGenerating = false;
     }

    // private void onImageGenerationFailed(){ }


    private void decideVisibleContent(String id){
        nothingGeneratedText.setVisible(id.equals(nothingGeneratedText.getId()));
        generatingImageIndicator.setVisible(id.equals(generatingImageIndicator.getId()));
        errorOccurredText.setVisible(id.equals(errorOccurredText.getId()));
        generatedImage.setVisible(id.equals(generatedImage.getId()));
    }


    private boolean confirmCancelGeneration(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    public void onBackIconClick(MouseEvent mouseEvent) {
        screenManager.entryScreen();
    }


    private File openDirectoryChooser(){
        SavableManager savableManager = SingletonFactory.getInstance(SavableManager.class);
        AppSettings settings = (AppSettings) savableManager.getSavableClass(AppSettings.class);

        DirectoryChooser directoryChooser = new  DirectoryChooser();
        directoryChooser.setTitle("Choose directory");
        if ( settings.getLastOpenedDirPath() != null )
            directoryChooser.setInitialDirectory(new File(settings.getLastOpenedDirPath()));

        return directoryChooser.showDialog(null);
    }
}
