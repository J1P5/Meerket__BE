package org.j1p5.api.product.converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MultipartFileConverter {

    public static List<File> convertMultipartFilesToFiles(List<MultipartFile> multipartFiles) {
        List<File> files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                // Create a temporary file
                File tempFile = File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
                multipartFile.transferTo(tempFile); // Write the content to the temp file
                files.add(tempFile);
                // Ensure the temporary file is deleted when the application exits
                tempFile.deleteOnExit();
            } catch (IOException e) {
                throw new RuntimeException("Error converting MultipartFile to File: " + multipartFile.getOriginalFilename(), e);
            }
        }

        return files;
    }
}
