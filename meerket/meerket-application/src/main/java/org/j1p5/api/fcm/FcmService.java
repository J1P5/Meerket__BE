package org.j1p5.api.fcm;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.fcm.FcmSender;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmSender fcmSender;
    private final UserRepository userRepository;

    public void sendFcmChatMessage(Long receiverId, Long userId, String content) {

        //TODO
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("예외처리"));

        fcmSender.sendPushChatMessageNotification(receiverId, userEntity.getNickname(), content);
    }






}
