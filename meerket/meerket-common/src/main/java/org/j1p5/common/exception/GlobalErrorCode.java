package org.j1p5.common.exception;

import lombok.Getter;

@Getter
public enum GlobalErrorCode implements BaseErrorCode{
    // 에러 코드 작성
    ;

    private final String errorCode;
    private final String message;

    GlobalErrorCode(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, errorCode, message);
    }

}
