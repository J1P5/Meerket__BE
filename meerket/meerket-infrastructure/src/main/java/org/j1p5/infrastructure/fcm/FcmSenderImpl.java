package org.j1p5.infrastructure.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.domain.fcm.FcmSender;
import org.j1p5.domain.fcm.entity.FcmTokenEntity;
import org.j1p5.domain.fcm.repository.FcmTokenRepository;
import org.j1p5.infrastructure.fcm.exception.FcmException;
import org.j1p5.infrastructure.global.exception.InfraException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.j1p5.infrastructure.fcm.exception.FcmException.AUCTION_BUYER_FCM_TOKEN_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmSenderImpl implements FcmSender {

    private final FcmTokenRepository fcmTokenRepository;


    // 채팅 상대방에게 푸쉬 보내기
    @Override
    public void sendPushChatMessageNotification(
            String roomId, Long receiverId, String senderNickname, String content) {
        try {
            FcmTokenEntity fcmTokenEntity =
                    fcmTokenRepository
                            .findByUserId(receiverId)
                            .orElseThrow(() -> new InfraException(FcmException.RECEIVER_NOT_FOUND));

            Notification notification =
                    Notification.builder()
                            .setTitle(senderNickname + " 님 에게 메시지가 도착했습니다.")
                            .setBody(content)
                            .build();

            Map<String, String> data = new HashMap<>();
            data.put("roomId", roomId);
            data.put("userId", receiverId.toString());


            Message message =
                    Message.builder()
                            .setToken(fcmTokenEntity.getToken())
                            .setNotification(notification)
                            .putAllData(data)
                            .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            log.error("fcm 채팅 메시지 보내기 실패", e);
        }
    }


    // 판매자에게 입찰이 +1이라는 푸쉬알림
    @Override
    public void sendPushSellerBidNotification(Long productId, Long userId, String title, String content) {
        try {
            FcmTokenEntity fcmTokenEntity = fcmTokenRepository.findByUserId(userId)
                    .orElseThrow(() -> new InfraException(FcmException.AUCTION_SELLER_FCM_TOKEN_NOT_FOUND));

            Notification notification =
                    Notification.builder()
                            .setTitle(title + " " + content)
                            .build();

            Map<String, String> data = new HashMap<>();
            data.put("productId", productId.toString());

            Message message =
                    Message.builder()
                            .setToken(fcmTokenEntity.getToken())
                            .setNotification(notification)
                            .putAllData(data)
                            .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            log.error("fcm 판매자에게 메시지 보내기 실패", e);
        }

    }

    @Override
    public void sendPushBuyerBidNotification(Long productId, List<Long> userIds, String title, String content) {
        try {
            List<FcmTokenEntity> fcmTokenEntities = fcmTokenRepository.findByUserIdIn(userIds);
            if(fcmTokenEntities.isEmpty()){
                log.info("사용자 fcm이 없음");
            }

            Notification notification = Notification.builder()
                    .setTitle(title + " " + content)
                    .build();
            for(FcmTokenEntity fcmToken : fcmTokenEntities){
                Map<String,String> data = new HashMap<>();
                data.put("productId", productId.toString());

                Message message = Message.builder()
                        .setToken(fcmToken.getToken())
                        .setNotification(notification)
                        .putAllData(data)
                        .build();
                FirebaseMessaging.getInstance().send(message);
            }
        }catch (Exception e){
            log.error("fcm 구매자에게 메세지 보내기 실패", e);
        }
    }
}
