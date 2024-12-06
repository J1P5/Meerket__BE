package org.j1p5.domain.fcm;

public interface FcmSender {

    void sendPushChatMessageNotification(String roomId, Long receiverId, String senderNickname, String content);

    void sendPushSellerBidNotification(Long productId, Long userId, String title, String content);


}
