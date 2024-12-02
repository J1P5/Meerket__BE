package org.j1p5.infrastructure.global.s3;

import static org.j1p5.infrastructure.global.s3.exception.S3ErrorCode.INVALID_FILE_EXTENSION;
import static org.j1p5.infrastructure.global.s3.exception.S3ErrorCode.NO_FILE_EXTENSION;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.j1p5.infrastructure.global.exception.InfraException;
import org.springframework.stereotype.Component;

@Component
public class ImageValidator {

    // 허용되는 확장자 목록
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    // 허용되는 MIME 타입 목록
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

    /** 이미지 파일 유효성 검사 (확장자 + MIME 타입) */
    public static void validateImageFile(File file) {
        String filename = file.getName();

        if (filename == null || filename.trim().isEmpty()) {
            throw new InfraException(NO_FILE_EXTENSION); // 예외 처리: 파일 이름 없음
        }

        // 확장자 검증
        validateFileExtension(filename);

        // MIME 타입 검증
        validateMimeType(file);
    }

    /** 파일 확장자 검증 */
    private static void validateFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            throw new InfraException(NO_FILE_EXTENSION); // 예외 처리: 확장자 없음
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new InfraException(INVALID_FILE_EXTENSION); // 예외 처리: 허용되지 않은 확장자
        }
    }

    /** MIME 타입 검증 */
    private static void validateMimeType(File file) {
        try {
            // 파일의 MIME 타입 추출
            String mimeType = Files.probeContentType(file.toPath());

            if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType)) {
                throw new InfraException(INVALID_FILE_EXTENSION); // 예외 처리: 허용되지 않은 MIME 타입
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to determine MIME type", e);
        }
    }
}
