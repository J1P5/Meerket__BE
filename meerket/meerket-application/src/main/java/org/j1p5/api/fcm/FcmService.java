package org.j1p5.api.fcm;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.chat.exception.ChatException;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.api.product.exception.ProductException;
import org.j1p5.domain.fcm.FcmSender;
import org.j1p5.domain.fcm.entity.FcmTokenEntity;
import org.j1p5.domain.fcm.repository.FcmTokenRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    // 채팅 알림
    public void sendFcmChatMessage(String roomId, Long receiverId, Long userId, String content) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new InfraException(ChatException.CHAT_RECEIVER_NOT_FOUND));

        fcmSender.sendPushChatMessageNotification(roomId,receiverId, userEntity.getNickname(), content);
    }

    // 판매자에게 입찰 알림
    public void sendSellerBidMessage(Long productId) {
        ProductEntity productEntity = getProductEntity(productId);

        String content = "상품에 누군가 입찰했어요!";
        sendPushSellerBidNotification(productEntity, content);
    }

    // 판매자에게 입찰 수정 알림
    public void sendSellerBidUpdateMessage(Long productId) {
        ProductEntity productEntity = getProductEntity(productId);

        String content = "상품에 입찰금액 변동이 발생했어요";
        sendPushSellerBidNotification(productEntity, content);
    }


    private void sendPushSellerBidNotification(ProductEntity productEntity, String content) {
        fcmSender.sendPushSellerBidNotification(
                productEntity.getId(), productEntity.getUser().getId(), productEntity.getTitle(), content
        );
    }

    private ProductEntity getProductEntity(Long productId) {
        ProductEntity productEntity = productRepository
                .findById(productId).orElseThrow(() -> new WebException(ProductException.PRODUCT_NOT_FOUND));
        return productEntity;
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
