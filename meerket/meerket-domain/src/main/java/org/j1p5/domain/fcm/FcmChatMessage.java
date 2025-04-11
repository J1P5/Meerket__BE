package org.j1p5.domain.fcm;

public record FcmChatMessage(
        Long receiverId, String senderNickname, String titleMessage, String content
) {
}
