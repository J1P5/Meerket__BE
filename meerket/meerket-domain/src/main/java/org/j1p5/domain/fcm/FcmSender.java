package org.j1p5.domain.fcm;

import java.util.List;

public interface FcmSender {

    void sendPushChatMessageNotification(FcmChatMessage fcmChatMessage, String uri);

    void sendPushSellerBidNotification(Long userId, String title, String titleMessage, String uri);

    void sendPushBuyerBidNotification(List<Long> userIds, String title, String titleMessage, String uri);


}
