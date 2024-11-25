package org.j1p5.api.product.service;

import org.j1p5.api.global.excpetion.WebException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

import static org.j1p5.api.product.exception.S3ErrorCode.INVALID_FILE_EXTENSION;
import static org.j1p5.api.product.exception.S3ErrorCode.NO_FILE_EXTENSION;

@Component
public class ImageValidator {

    // 허용되는 확장자 목록
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    // 허용되는 MIME 타입 목록
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

    /**
     * 이미지 파일 유효성 검사 (확장자 + MIME 타입)
     */
    public static void validateImageFile(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if (filename == null || filename.trim().isEmpty()) {
            throw new WebException(NO_FILE_EXTENSION); // 예외 처리: 파일 이름 없음
        }

        // 확장자 검증
        validateFileExtension(filename);

        // MIME 타입 검증
        validateMimeType(file);
    }

    /**
     * 파일 확장자 검증
     */
    private static void validateFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            throw new WebException(NO_FILE_EXTENSION); // 예외 처리: 확장자 없음
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new WebException(INVALID_FILE_EXTENSION); // 예외 처리: 허용되지 않은 확장자
        }
    }

    /**
     * MIME 타입 검증
     */
    private static void validateMimeType(MultipartFile file) {
        String mimeType = file.getContentType();

        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new WebException(INVALID_FILE_EXTENSION); // 예외 처리: 허용되지 않은 MIME 타입
        }
    }
}
