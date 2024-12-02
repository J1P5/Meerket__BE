package org.j1p5.infrastructure.global.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum InfraErrorCode implements BaseErrorCode {
    BAD_REQUEST(400, "INFRA400", "bad request"),
    BAD_CREDENTIAL(401, "INFRA401", "bad credential"),
    INVALID_AUTHORIZATION(403, "INFRA403", "invalid authorization"),
    UNKNOWN_ERROR(500, "INFRA500", "unknown error");

    private final int status;
    private final String errorCode;
    private final String message;

    InfraErrorCode(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}
