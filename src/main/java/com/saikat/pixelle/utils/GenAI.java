package com.saikat.pixelle.utils;

import com.google.genai.Client;
import com.google.genai.types.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.*;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

public class GenAI {
    public  static void saveBinaryFile(String fileName, byte[] content) {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(content);
            out.close();
            System.out.println("Saved file: " + fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("GEMINI_API_KEY");
        Client client = Client.builder().apiKey(apiKey).build();
        Gson gson = new Gson();
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();


        String model = "gemini-2.0-flash-preview-image-generation";
        List<Content> contents = ImmutableList.of(
                Content.builder()
                        .role("user")
                        .parts(ImmutableList.of(
                                Part.fromText("Generate a image of a imaginary horse.")
                        ))
                        .build()
        );
        GenerateContentConfig config =
                GenerateContentConfig
                        .builder()
                        .responseModalities(ImmutableList.of(
                                "IMAGE",
                                "TEXT"
                        ))
                        .responseMimeType("text/plain")
                        .build();

        ResponseStream<GenerateContentResponse> responseStream = client.models.generateContentStream(model, contents, config);

        for (GenerateContentResponse res : responseStream) {
            if (res.candidates().isEmpty() || res.candidates().get().getFirst().content().isEmpty() || res.candidates().get().get(0).content().get().parts().isEmpty()) {
                continue;
            }

            List<Part> parts = res.candidates().get().getFirst().content().get().parts().get();
            for (Part part : parts) {
                if (part.inlineData().isPresent()) {
                    String fileName = "ai_generated_horse2";
                    Blob inlineData = part.inlineData().get();
                    String fileExtension;
                    try {
                        fileExtension = allTypes.forName(inlineData.mimeType().orElse("")).getExtension();
                    } catch (MimeTypeException e) {
                        fileExtension = "";
                    }
                    saveBinaryFile(fileName + "." + fileExtension, inlineData.data().get());
                }
                else {
                    System.out.println(part.text());
                }
            }
        }

        responseStream.close();
    }
}

