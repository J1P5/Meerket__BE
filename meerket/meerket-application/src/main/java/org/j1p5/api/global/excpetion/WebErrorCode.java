package org.j1p5.api.global.excpetion;

import lombok.Getter;
import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

@Getter
public enum WebErrorCode implements BaseErrorCode {
    USER_NOT_FOUND(404, "USER404", "해당 유저가 존재하지 않습니다"),
    NICKNAME_ALREADY_EXIST(409, "USER409", "이미 존재하는 닉네임입니다."),
    ;
    private final int status;
    private final String errorCode;
    private final String message;

    WebErrorCode(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}