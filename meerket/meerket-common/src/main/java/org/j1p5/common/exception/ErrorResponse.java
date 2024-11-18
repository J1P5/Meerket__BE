package org.j1p5.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private boolean isSuccess;
    private String errorCode;
    private String message;

    private ErrorResponse(boolean isSuccess, String errorCode, String message) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse of(boolean isSuccess, String errorCode, String message) {
        return new ErrorResponse(isSuccess, errorCode, message);
    }
}
