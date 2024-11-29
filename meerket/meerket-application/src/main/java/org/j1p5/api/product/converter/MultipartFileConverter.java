package org.j1p5.api.product.converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultipartFileConverter {

    public static List<File> convertMultipartFilesToFiles(List<MultipartFile> multipartFiles) {
        List<File> files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                // Create a temporary file
                File tempFile = File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
                multipartFile.transferTo(tempFile);
                files.add(tempFile);
                tempFile.deleteOnExit();
            } catch (IOException e) {
                throw new RuntimeException("Error converting MultipartFile to File: " + multipartFile.getOriginalFilename(), e);
            }
        }

        return files;
    }
}
