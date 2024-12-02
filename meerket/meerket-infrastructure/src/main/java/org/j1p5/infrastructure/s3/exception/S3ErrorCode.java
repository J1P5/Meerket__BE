package org.j1p5.infrastructure.s3.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum S3ErrorCode implements BaseErrorCode {
    EMPTY_FILE_EXCEPTION(400, "FILE400", "빈 파일입니다"),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(500, "FILE500", "이미지 업로드 중 IO 예외가 발생했습니다"),
    IO_EXCEPTION_ON_FILE_UPLOAD(500, "FILE501", "파일 업로드 중 IO 예외가 발생했습니다"),
    NO_FILE_EXTENSION(400, "FILE402", "파일 확장자가 없습니다"),
    INVALID_FILE_EXTENSION(400, "FILE403", "유효하지 않은 파일 확장자입니다"),
    INVALID_DOCUMENT_EXTENSION(400, "FILE404", "유효하지 않은 문서 확장자입니다"),
    PUT_OBJECT_EXCEPTION(500, "FILE405", "파일 업로드 중 오류가 발생했습니다"),
    IO_EXCEPTION_ON_IMAGE_DELETE(500, "FILE406", "이미지 삭제 중 IO 예외가 발생했습니다"),
    IO_EXCEPTION_ON_FILE_DELETE(500, "FILE407", "파일 삭제 중 IO 예외가 발생했습니다");

    private final int status;
    private final String errorCode;
    private final String message;

    S3ErrorCode(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}
