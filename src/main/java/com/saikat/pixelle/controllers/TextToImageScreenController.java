package com.saikat.pixelle.controllers;

import com.saikat.pixelle.components.CustomMenu;
import com.saikat.pixelle.listeners.OnImageGeneratedListener;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.GenAIUtil;
import com.saikat.pixelle.utils.FileChooserUtil;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TextToImageScreenController {

    @FXML public VBox      mainGridLayout;
    @FXML public Button    generateButton;
    @FXML public TextField imagePrompt;
    @FXML public Label     nothingGeneratedText;
    @FXML public VBox      generatingImageIndicator;
    @FXML public Label     errorOccurredText;
    @FXML public HBox      generatedImage;
    @FXML public ImageView imageView;
    @FXML public CustomMenu menu;

    private boolean       isGenerating = false;
    private ScreenManager screenManager;
    private GenAIUtil     genAI;
    private AppSettings   appSettings;
    private String        generatedImagePath;

    private ProgressIndicator progressIndicator;

    public void initialize() {

        SavableManager savableManager = SingletonFactoryUtil.getInstance(SavableManager.class);
        appSettings   = (AppSettings) savableManager.getSavableClass(AppSettings.class);
        genAI         = new GenAIUtil();
        screenManager = SingletonFactoryUtil.getInstance(ScreenManager.class);
        generateButton.setDisable(true);

        imagePrompt.setOnKeyPressed( e -> {
            if ( e.getCode().equals(KeyCode.ENTER) ){
                onGenerateButtonClick(null);
            } else {
                generateButton.setDisable(imagePrompt.getText().isEmpty());
                appSettings.setLastImageGenPrompt(imagePrompt.getText() + e.getText());
            }
        });

        progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(19, 20);

        generateButton.setOnAction(this::onGenerateButtonClick);

        String lastPrompt = appSettings.getLastImageGenPrompt();
        if ( lastPrompt != null ){
            this.imagePrompt.setText(lastPrompt);
            generateButton.setDisable(false);
        }

        // setting up menus
        Map<String, List<String>> mp = new HashMap<>();
        mp.put("File", List.of(new String[]{ "Save", "Save As", "Open" }));
        mp.put("Tools", List.of(new String[]{ "Package", "Go Back", "Clear Prompt" }));
        menu.setMenus(mp);

    }

    private void onGenerateButtonClick(ActionEvent event) {
        if( isGenerating && confirmCancelGeneration() ) {
            generateButton.setText("Generate");
            imagePrompt.setDisable(false);
            generateButton.setGraphic(null);
            decideVisibleContent(nothingGeneratedText.getId());
            genAI.cancelGeneration();
            isGenerating = false;
        } else  {
            System.out.println("Started generating....");
            generateButton.setText("Generating...");
            imagePrompt.setDisable(true);
            generateButton.setGraphic(progressIndicator);
            decideVisibleContent(generatingImageIndicator.getId());
            isGenerating = true;

            String desc = imagePrompt.getText();
            genAI.generateImage(desc, new OnImageGeneratedListener() {
                @Override
                public void onImageGenerated(String imagePath) {
                    System.out.println(imagePath);
                    Platform.runLater(() -> {
                        onImageGenerationComplete(imagePath);
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    System.out.println(errorMessage);
                    Platform.runLater(() -> {
                        onImageGenerationFailed(errorMessage);
                    });
                }
            });
        }
    }

     private void onImageGenerationComplete(String generatedImagePath){
         decideVisibleContent(generatedImage.getId());

         String imagePath = "file:" + generatedImagePath;
         Image image = new Image(imagePath);
         imageView.setImage(image);
         this.generatedImagePath = generatedImagePath;

         generateButton.setText("Generate");
         imagePrompt.setDisable(false);
         generateButton.setGraphic(null);
         isGenerating = false;
     }

     private void onImageGenerationFailed(String errorMessage) {
         decideVisibleContent(errorOccurredText.getId());
         generateButton.setText("Generate");
         imagePrompt.setDisable(false);
         generateButton.setGraphic(null);
         isGenerating = false;
     }


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
        genAI.cancelGeneration();
        screenManager.entryScreen();
    }

    private void saveFile(File file, String fileName) throws IOException {
        File destination = FileChooserUtil.getSaveFile(fileName);
        if (destination != null) {
            Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @FXML
    public void saveButtonClicked(MouseEvent mouseEvent) {
        File file = new File(generatedImagePath);
        int len = appSettings.getLastImageGenPrompt().length();
        String fileName = appSettings.getLastImageGenPrompt().substring(0, Math.min(len, 20)).toLowerCase() + ".png";
        try {
            saveFile(file, fileName);
        } catch (IOException e) {
            System.err.println("Could not save generated image. Maybe show an error alert.");
        }
    }
}