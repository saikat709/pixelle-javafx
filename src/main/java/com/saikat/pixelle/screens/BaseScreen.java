package com.saikat.pixelle.screens;

import com.saikat.pixelle.PhotoEditorApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.util.Objects;

public class BaseScreen {
    protected String fxmlFile;
    protected String cssFile;
    protected String title;

    private Scene scene;

    protected BaseScreen(String fxmlFile, String cssFile, String title) {
        this.fxmlFile = fxmlFile;
        this.cssFile = cssFile;
        this.title = title;
        setScene();
    }

    protected BaseScreen(String fxmlFile, String title) {
        this(fxmlFile, null, title);
    }

    protected BaseScreen() {
        this(null, null, null);
    }

    private void setScene(){
        if ( fxmlFile == null )
            throw new IllegalArgumentException("fxmlFile cannot be null");

        FXMLLoader loader = new FXMLLoader(PhotoEditorApplication.class.getResource(fxmlFile));
        loader.setResources(null);

        try {
            this.scene = new Scene(loader.load(), 720, 540);
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            PhotoEditorApplication.class.getResource("styles/main.css")
                    ).toExternalForm()
            );
            if (  cssFile != null ) {
                scene.getStylesheets().add(
                        Objects.requireNonNull(
                                PhotoEditorApplication.class.getResource(cssFile)
                        ).toExternalForm()
                );
            }

        } catch (Exception e) {
            System.out.println("Failed to load scene: IO Exception  inside setScene(): " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        if ( scene == null ){
            throw new IllegalStateException("Scene is not initialized yet");
        }
        return scene;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }

    public String getCssFile() {
        return cssFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFxmlFile(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public void setCssFile(String cssFile) {
        this.cssFile = cssFile;
    }

    @Override
    public String toString() {
        return "BaseScreen{" +
                "fxmlFile='" + fxmlFile + '\'' +
                ", cssFile='" + cssFile + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
