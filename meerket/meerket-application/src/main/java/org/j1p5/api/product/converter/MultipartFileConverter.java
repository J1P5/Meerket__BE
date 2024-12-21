package org.j1p5.api.product.converter;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultipartFileConverter {

    /**
     * 멀티파트 파일에서 파일로 변환
     * author sunghyun
     * @param multipartFiles
     * @return
     */
    public static List<File> convertMultipartFilesToFiles(List<MultipartFile> multipartFiles) {
        List<File> files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                // Create a temporary file
                File tempFile =
                        File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
                multipartFile.transferTo(tempFile);
                files.add(tempFile);
                tempFile.deleteOnExit();
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error converting MultipartFile to File: "
                                + multipartFile.getOriginalFilename(),
                        e);
            }
        }

        return files;
    }

    public static File convertMultipartFileToFile(MultipartFile multipartFile) {
        File file;

        try {
            file = File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
            multipartFile.transferTo(file); // Write the content to the temp file
        } catch (IOException e) {
            throw new RuntimeException("Error converting MultipartFile to File: " + multipartFile.getOriginalFilename(), e);
        }

        return file;
    }
}
