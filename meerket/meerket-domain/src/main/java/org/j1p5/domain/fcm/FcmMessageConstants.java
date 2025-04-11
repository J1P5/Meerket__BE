package org.j1p5.domain.fcm;

import lombok.Getter;

@Getter
public enum FcmMessageConstants {
    BID_ALERT("상품에 누군가 입찰했어요!"),
    BID_UPDATE("상품에 입찰금액 변동이 발생했어요"),
    BID_CANCEL("상품에 입찰이 취소되었어요"),
    BID_EARLY_CLOSED("판매자가 판매 조기마감을 하여 2시간뒤에 입찰이 마감됩니다"),
    BID_DELETED("판매자가 게시물을 삭제하였습니다. 해당 입찰은 유효하지 않습니다."),
    CHAT_ALERT("님에게 메시지가 도착했습니다.");

    private String message;

    FcmMessageConstants(String message) {
    }
}
