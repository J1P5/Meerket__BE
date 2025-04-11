package org.j1p5.api.fcm.type;

import lombok.Getter;

@Getter
public enum FcmMessageType {
    BID_ALERT("상품에 누군가 입찰했어요!", FcmRedirectUri.PRODUCT_DETAIL),
    BID_UPDATE("상품에 입찰금액 변동이 발생했어요", FcmRedirectUri.PRODUCT_DETAIL),
    BID_CANCEL("상품에 입찰이 취소되었어요", FcmRedirectUri.PRODUCT_DETAIL),
    BID_EARLY_CLOSED("판매자가 판매 조기마감을 하여 2시간뒤에 입찰이 마감됩니다", FcmRedirectUri.PRODUCT_DETAIL),
    BID_DELETED("판매자가 게시물을 삭제하였습니다. 해당 입찰은 유효하지 않습니다.", FcmRedirectUri.HOME),
    CHAT_ALERT("님에게 메시지가 도착했습니다.", FcmRedirectUri.CHATTING);

    private String message;
    private FcmRedirectUri redirectUri;

    FcmMessageType(String message, FcmRedirectUri redirectUri) {
    }
}
