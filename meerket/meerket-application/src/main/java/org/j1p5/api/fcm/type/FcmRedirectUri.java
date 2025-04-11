package org.j1p5.api.fcm.type;

import lombok.Getter;
import org.j1p5.api.fcm.exception.FcmRedirectException;

import static org.j1p5.api.global.excpetion.WebErrorCode.FCM_MESSAGE_NULL_POINTER;

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

    public String buildUri(String... pathVariables) {
        if (pathVariables == null) {
            throw new FcmRedirectException(FCM_MESSAGE_NULL_POINTER);
        }
        StringBuilder builder = new StringBuilder(getUri());
        for (String pathVariable : pathVariables) {
            if (pathVariable == null) {
                throw new FcmRedirectException(FCM_MESSAGE_NULL_POINTER);
            }
            if (!builder.toString().endsWith("/")) {
                builder.append("/");
            }
            builder.append(pathVariable);
        }
        return builder.toString();
    }
}
