package org.j1p5.infrastructure.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.j1p5.domain.fcm.FcmService;
import org.j1p5.domain.fcm.entity.FcmTokenEntity;
import org.j1p5.infrastructure.fcm.repository.FcmTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {

    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public void sendPushChatMessageNotification(
            Long receiverId, String senderNickname, String content) {
        try {
            FcmTokenEntity fcmTokenEntity =
                    fcmTokenRepository
                            .findByUserId(receiverId)
                            .orElseThrow(() -> new IllegalArgumentException("상대방이 존재하지 않습니다."));

            Notification notification =
                    Notification.builder()
                            .setTitle(senderNickname + " 님 에게 메시지가 도착했습니다.")
                            .setBody(content)
                            .build();

            Message message =
                    Message.builder()
                            .setToken(fcmTokenEntity.getToken())
                            .setNotification(notification)
                            .build();

            FirebaseMessaging.getInstance().send(message);
            // TODO 커스텀 예외 처리
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
