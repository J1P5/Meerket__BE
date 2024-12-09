package org.j1p5.api.auction.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum AuctionException implements BaseErrorCode {
    AUCTION_MIN_PRICE_ERROR(400, "AUCTION_MIN_PRICE_400", "최소 입찰 가격 미만입니다."),
    BID_USER_NOT_FOUND(404, "BID_USER_404", "입찰자가 존재하지 않습니다."),
    AUCTION_SELLER_FCM_TOKEN_NOT_FOUND(404, "SELLER_FCM_NOT_FOUND", "판매자가 존재하지 않습니다."),
    BID_NOT_FOUND(404, "AUCTION_BID_404", "해당 입찰을 찾을 수 없습니다."),
    BID_USER_NOT_AUTHORIZED(403, "BID_USER_403", "해당 입찰에 대한 권한이 없습니다."),
    BID_AMOUNT_TOO_LOW(400, "BID_AMOUNT_400", "입찰 금액이 기존 입찰 금액보다 낮습니다."),
    DUPLICATE_BID(400, "DUPLICATE_BID", "이미 입찰한 기록이 있습니다."),
    SELLER_CANNOT_CREATE_BID(500,"SELLER_BID_500","판매자는 판매글에 입찰을 할 수 없습니다.")

    ;






    ;



    private final int status;
    private final String errorCode;
    private final String message;

    AuctionException(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }


    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}
