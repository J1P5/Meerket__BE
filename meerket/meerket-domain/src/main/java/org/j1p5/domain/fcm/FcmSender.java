package org.j1p5.domain.fcm;

import java.util.List;

public interface FcmSender {

    void sendPushChatMessageNotification(Long receiverId, String senderNickname, String content, String uri);

    void sendPushSellerBidNotification(Long userId, String title, String content, String uri);

    void sendPushBuyerBidNotification(List<Long> userIds, String title, String content, String uri);


}
