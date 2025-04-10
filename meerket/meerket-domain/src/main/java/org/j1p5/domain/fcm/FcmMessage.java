package org.j1p5.domain.fcm;

public record FcmMessage(
        String title,
        String titlePostMessage,
        String body
) {
}
