package org.j1p5.domain.global.exception;

import lombok.Getter;
import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

@Getter
public enum DomainErrorCode implements BaseErrorCode {
    INVALID_PROVIDER(400, "AUTH400", "invalid provider"),
    USER_NOT_FOUND(401, "AUTH401", "user not found");

    private final int status;
    private final String errorCode;
    private final String message;

    DomainErrorCode(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}
