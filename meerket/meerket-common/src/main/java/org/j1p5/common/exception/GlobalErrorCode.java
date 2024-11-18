package org.j1p5.common.exception;

import lombok.Getter;

@Getter
public enum GlobalErrorCode implements BaseErrorCode{
    // 에러 코드 작성
    ;
    private final int status;
    private final String errorCode;
    private final String message;

    GlobalErrorCode(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }

}
