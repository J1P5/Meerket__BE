package org.j1p5.domain.product.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum ProductException implements BaseErrorCode {

    // 동네 인증관련 에러
    REGION_AUTH_NOT_FOUND(404, "REGION404", "사용자의 동네 인증 정보가 없습니다."),
    ;
    private final int status;
    private final String errorCode;
    private final String message;

    ProductException(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}
