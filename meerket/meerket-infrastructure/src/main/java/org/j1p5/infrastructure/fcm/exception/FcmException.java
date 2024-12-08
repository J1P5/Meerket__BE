package org.j1p5.infrastructure.fcm.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum FcmException implements BaseErrorCode {

    // 채팅 관련
    RECEIVER_NOT_FOUND(404, "FCM404", "상대방을 찾을 수 없습니다."),

    // 경매 관련
    AUCTION_SELLER_FCM_TOKEN_NOT_FOUND(404, "SELLER_FCM_NOT_FOUND", "판매자가 존재하지 않습니다."),

    AUCTION_BUYER_FCM_TOKEN_NOT_FOUND(404,"BUYER_FCM_NOT_FOUN","구매자들의 FCM토큰이 존재하지 않습니다"),


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
        return ErrorResponse.of(false, status, errorCode, message);
    }

}
