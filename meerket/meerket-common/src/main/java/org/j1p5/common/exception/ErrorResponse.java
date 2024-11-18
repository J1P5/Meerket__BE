package org.j1p5.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private boolean isSuccess;
    private int status;
    private String errorCode;
    private String message;

    private ErrorResponse(boolean isSuccess, int status, String errorCode, String message) {
        this.isSuccess = isSuccess;
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse of(boolean isSuccess, int status, String errorCode, String message) {
        return new ErrorResponse(isSuccess, status, errorCode, message);
    }
}
