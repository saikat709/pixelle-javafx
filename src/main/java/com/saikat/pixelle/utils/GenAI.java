package com.saikat.pixelle.utils;

import com.google.genai.Client;
import com.google.genai.types.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.genai.ResponseStream;
import com.google.gson.Gson;

import com.saikat.pixelle.listeners.OnImageGeneratedListener;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import static com.saikat.pixelle.constants.ConstValues.*;

public class GenAI {
    private OnImageGeneratedListener listener;
    private Thread apiThread;

    public void saveBinaryFile(String fileName, byte[] content) {
        try {
            File file = new File(BASE_DIR, fileName);
            FileOutputStream out = new FileOutputStream(file);
            out.write(content);
            out.close();
            listener.onImageGenerated(file.getAbsolutePath());
            System.out.println("Saved file: " + fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void generateImage(String description) {

        String apiKey = System.getenv("GEMINI_API_KEY");
        Client client = Client.builder().apiKey(apiKey).build();
        Gson gson = new Gson();
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();

        List<Content> contents = ImmutableList.of(
                Content.builder()
                        .role("user")
                        .parts(ImmutableList.of(
                                Part.fromText("Generate a image based on the text, strictly use 1:1 aspect ratio: " + description)
                        ))
                        .build()
        );
        GenerateContentConfig config =
                GenerateContentConfig
                        .builder()
                        .responseModalities(ImmutableList.of(
                                "IMAGE", "TEXT"
                        ))
                        .responseMimeType("text/plain")
                        .build();

        ResponseStream<GenerateContentResponse> responseStream = client.models.generateContentStream(GEMINI_IMAGE_MODEL, contents, config);

        boolean imageGenerated = false;
        for (GenerateContentResponse res : responseStream) {
            if (Thread.currentThread().isInterrupted()) {
                responseStream.close();
                return;
            }
            if (res.candidates().isEmpty() || res.candidates().get().getFirst().content().isEmpty() || res.candidates().get().get(0).content().get().parts().isEmpty()) {
                continue;
            }

            List<Part> parts = res.candidates().get().getFirst().content().get().parts().get();
            for (Part part : parts) {
                if (part.inlineData().isPresent()) {
                    Blob inlineData = part.inlineData().get();
                    String fileExtension;
                    try {
                        fileExtension = allTypes.forName(inlineData.mimeType().orElse("")).getExtension();
                    } catch (MimeTypeException e) {
                        fileExtension = "";
                    }
                    saveBinaryFile(GENERATED_FILENAME + "." + fileExtension, inlineData.data().get());
                    imageGenerated = true;
                }
                else {
                    System.out.println(part.text());
                }
            }
        }
        responseStream.close();
        if ( !imageGenerated ) this.listener.onError("Could not generate image.");
    }


    public void cancelGeneration(){
        if ( apiThread != null && apiThread.isAlive() ) apiThread.interrupt();
    }

    public void generateImage(String description, OnImageGeneratedListener  listener){
        this.listener = listener;

        if ( listener != null && description == null ) listener.onError( "Description cannot be null");
        if ( listener == null ) throw new NullPointerException("OnImageGeneratedListener cannot be null");

        apiThread = new Thread( () -> {
            generateImage(description);
        });
        apiThread.start();
    }
}

