package org.j1p5.infrastructure.fcm.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum FcmException implements BaseErrorCode {
    RECEIVER_NOT_FOUND(404, "FCM404", "상대방을 찾을 수 없습니다."),




    ;
    private final int status;
    private final String errorCode;
    private final String message;

    FcmException(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return null;
    }

}
