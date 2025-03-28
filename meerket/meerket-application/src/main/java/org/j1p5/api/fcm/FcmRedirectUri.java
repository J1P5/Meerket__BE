package org.j1p5.api.fcm;

import lombok.Getter;

@Getter
public enum FcmRedirectUri {
    PRODUCT_DETAIL("/product/"),
    HOME("/"),
    CHATTING("/chatroom/")
    ;

    private final String uri;

    FcmRedirectUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return uri;
    }
}
