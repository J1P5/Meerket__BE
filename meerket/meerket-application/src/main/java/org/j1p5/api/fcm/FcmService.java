package org.j1p5.api.fcm;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.chat.exception.ChatException;
import org.j1p5.domain.fcm.FcmSender;
import org.j1p5.domain.fcm.entity.FcmTokenEntity;
import org.j1p5.domain.fcm.repository.FcmTokenRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.j1p5.infrastructure.global.exception.InfraException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmSender fcmSender;
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;

    public void sendFcmChatMessage(String roomId, Long receiverId, Long userId, String content) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new InfraException(ChatException.CHAT_RECEIVER_NOT_FOUND));

        fcmSender.sendPushChatMessageNotification(roomId,receiverId, userEntity.getNickname(), content);
    }


    // 사용자 로그인 했을때 fcm 토큰 추가 or 업데이트
    public void saveFcmToken(Long userId, String fcmToken) {
        //TODO 사용자 찾지못했을때의 커스텀 예외 처리
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        fcmTokenRepository.findByUserId(userId)
                .ifPresentOrElse(existingToken -> {
                    if(existingToken.getToken().equals(fcmToken)) return;

                    existingToken.updateToken(fcmToken);
                    fcmTokenRepository.save(existingToken);
                }, () -> {
                    fcmTokenRepository.save(FcmTokenEntity.create(userEntity, fcmToken));
                });

    }










}
