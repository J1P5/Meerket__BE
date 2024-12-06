package org.j1p5.api.product.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum ProductException implements BaseErrorCode {

    REGION_AUTH_NOT_FOUND(404, "REGION404", "사용자의 동네 인증 정보가 없습니다."),
    PRODUCT_NOT_FOUND(404, "PRODUCT404", "상품 조회에 실패하였습니다."),
    PRODUCT_NOT_AUTHORIZED(403, "PRODUCT403", "상품 수정 권한이 없습니다."),
    PRODUCT_HAS_BUYER(4050, "PRODUCT400", "입찰자가 있는 상품은 수정할 수 없습니다."),
    PRODUCT_HAS_NO_BUYER(400,"PRODUCT400","입찰자가 없어서 조기마감을 할 수 없습니다."),
    PRODUCT_IS_DELETED(410, "PRODUCT410", "삭제된 게시물입니다."),
    INVALID_PRODUCT_CATEGORY(400, "PRODUCT400","존재하지 않는 카테고리입니다."),
    INVALID_PRODUCT_KEYWORD(400, "PRODUCT401","올바르지 않은 검색내용입니다."),
    INVALID_PRODUCT_EARLY_CLOSED(400,"PRODUCT402","이미 마감시간이 2시간 이하이므로 조기마감할 수 없습니다."),
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
