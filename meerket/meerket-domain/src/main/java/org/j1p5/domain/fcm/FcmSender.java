package org.j1p5.domain.fcm;

public interface FcmSender {

    void sendPushChatMessageNotification(Long receiverId, String senderNickname, String content);

}
