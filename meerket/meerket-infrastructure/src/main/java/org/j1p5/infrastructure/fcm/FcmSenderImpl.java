package org.j1p5.infrastructure.fcm;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.domain.fcm.FcmChatMessage;
import org.j1p5.domain.fcm.FcmSender;
import org.j1p5.domain.fcm.entity.FcmTokenEntity;
import org.j1p5.domain.fcm.repository.FcmTokenRepository;
import org.j1p5.infrastructure.fcm.exception.FcmException;
import org.j1p5.infrastructure.global.exception.InfraException;
import org.j1p5.infrastructure.global.property.FrontServerProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.j1p5.infrastructure.fcm.exception.FcmException.valueOf;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmSenderImpl implements FcmSender {

    private final FcmTokenRepository fcmTokenRepository;
    private final FrontServerProperty frontServerProperty;

    // 채팅 상대방에게 푸쉬 보내기
    @Override
    public void sendPushChatMessageNotification(
            FcmChatMessage fcmChatMessage, String uri
    ) {
        try {
            FcmTokenEntity fcmTokenEntity =
                    fcmTokenRepository
                            .findByUserId(fcmChatMessage.receiverId())
                            .orElseThrow(() -> new InfraException(FcmException.RECEIVER_NOT_FOUND));

            Message message = buildFcmMessage(
                    fcmChatMessage.senderNickname(),
                    fcmChatMessage.titleMessage(),
                    fcmChatMessage.content(),
                    fcmTokenEntity.getToken(), uri);

            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            log.error("fcm 채팅 메시지 보내기 실패", e);
        }
    }


    // 판매자에게 입찰이 +1이라는 푸쉬알림
    @Override
    public void sendPushSellerBidNotification(
            Long userId, String title, String titleMessage, String uri
    ) {
        try {
            FcmTokenEntity fcmTokenEntity = fcmTokenRepository.findByUserId(userId)
                    .orElseThrow(() -> new InfraException(FcmException.AUCTION_SELLER_FCM_TOKEN_NOT_FOUND));

            Map<String, String> data = new HashMap<>();
            data.put("uri", uri);

            Message message = buildFcmMessage(title, titleMessage, fcmTokenEntity.getToken(), uri);

            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            log.error("fcm 판매자에게 메시지 보내기 실패", e);
        }
    }

    @Override
    public void sendPushBuyerBidNotification(
            List<Long> userIds, String title, String titleMessage, String uri
    ) {
        try {
            List<FcmTokenEntity> fcmTokenEntities = fcmTokenRepository.findByUserIdIn(userIds);

            if (fcmTokenEntities.isEmpty()) {
                log.info("사용자 fcm이 없음");
            }

            for(FcmTokenEntity fcmToken : fcmTokenEntities){
                Message message = buildFcmMessage(title, titleMessage, fcmToken.getToken(), uri);
                FirebaseMessaging.getInstance().send(message);
            }
        } catch (Exception e) {
            log.error("fcm 구매자에게 메세지 보내기 실패", e);
        }
    }

    private Message buildFcmMessage(
            String titleTarget, String titleMessage, String content, String token, String uri
    ) {
        Map<String, String> data = new HashMap<>();
        data.put("uri", uri);

        return Message.builder()
                .setNotification(buildNotification(titleTarget, titleMessage, content))
                .setToken(token)
                .setWebpushConfig(webPushConfigWithLink(uri))
                .putAllData(data)
                .build();
    }

    private Message buildFcmMessage(
            String titleTarget, String titleMessage, String token, String uri
    ) {
        Map<String, String> data = new HashMap<>();
        data.put("uri", uri);

        return Message.builder()
                .setNotification(buildNotification(titleTarget, titleMessage))
                .setToken(token)
                .setWebpushConfig(webPushConfigWithLink(uri))
                .putAllData(data)
                .build();
    }

    private Notification buildNotification(String target, String titleMessage, String content) {
        return Notification.builder()
                .setTitle(target + " " + titleMessage)
                .setBody(content)
                .build();
    }

    private Notification buildNotification(String target, String titleMessage) {
        return Notification.builder()
                .setTitle(target + " " + titleMessage)
                .build();
    }

    private WebpushConfig webPushConfigWithLink(String uri) {
        String link = frontServerProperty.baseUri() + uri;

        return WebpushConfig.builder()
                .setFcmOptions(WebpushFcmOptions.withLink(link))
                .build();
    }
}
