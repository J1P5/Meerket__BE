package org.j1p5.api.chat.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum ChatException implements BaseErrorCode {

    // 채팅관련 에러
    RECEIVER_NOT_FOUND(404, "RECEIVER404", "상대방이 존재하지 않습니다."),


    ;

    private final int status;
    private final String errorCode;
    private final String message;

    ChatException(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }


    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}
