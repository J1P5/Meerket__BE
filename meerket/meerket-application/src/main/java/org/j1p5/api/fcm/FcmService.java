package org.j1p5.api.fcm;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.chat.exception.ChatException;
import org.j1p5.api.fcm.type.FcmMessageType;
import org.j1p5.api.fcm.type.FcmRedirectUri;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.api.product.exception.ProductException;
import org.j1p5.api.user.exception.UserNotFoundException;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.j1p5.domain.fcm.FcmChatMessage;
import org.j1p5.domain.fcm.FcmSender;
import org.j1p5.domain.fcm.entity.FcmTokenEntity;
import org.j1p5.domain.fcm.repository.FcmTokenRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.j1p5.infrastructure.global.exception.InfraException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.j1p5.api.global.excpetion.WebErrorCode.USER_NOT_FOUND;
import static org.j1p5.api.fcm.type.FcmMessageType.*;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmSender fcmSender;
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final ProductRepository productRepository;
    private final AuctionRepository auctionRepository;

    // 채팅 알림
    public void sendFcmChatMessage(String roomId, Long receiverId, Long userId, String content) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new InfraException(ChatException.CHAT_RECEIVER_NOT_FOUND));

        sendPushChatNotification(
                roomId,
                receiverId,
                userEntity.getNickname(),
                content,
                CHAT_ALERT
        );
    }

    // 판매자에게 입찰 알림
    public void sendSellerBidMessage(Long productId) {
        notifySeller(getProductEntity(productId), BID_ALERT);
    }

    // 판매자에게 입찰 수정 알림
    public void sendSellerBidUpdateMessage(Long productId) {
        notifySeller(getProductEntity(productId), BID_UPDATE);
    }

    // 판매자에게 입찰 취소 알림
    public void sendSellerBidCancelMessage(Long productId) {
        notifySeller(getProductEntity(productId), BID_CANCEL);
    }

    // 조기마감 시 구매자에게 알림
    public void sendBuyerCloseEarlyMessage(Long productId) {
        notifyBuyers(getProductEntity(productId), BID_EARLY_CLOSED);
    }

    // 상품 삭제되었을 때 구매자에게 알림
    public void sendBuyerProductDeleted(Long productId){
        notifyBuyers(getProductEntity(productId), BID_DELETED);
    }

    private ProductEntity getProductEntity(Long productId) {
        return productRepository
                .findById(productId).orElseThrow(() -> new WebException(ProductException.PRODUCT_NOT_FOUND));
    }

    // 사용자 로그인 했을때 fcm 토큰 추가 or 업데이트
    public void saveFcmToken(Long userId, String fcmToken) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        fcmTokenRepository.findByUserId(userId)
                .ifPresentOrElse(existingToken -> {
                    if(!existingToken.getToken().equals(fcmToken)) {
                        existingToken.updateToken(fcmToken);
                        fcmTokenRepository.save(existingToken);
                    }
                }, () -> fcmTokenRepository.save(FcmTokenEntity.create(userEntity, fcmToken)));
    }

    private void notifySeller(ProductEntity product, FcmMessageType fcmType) {
        sendPushSellerBidNotification(
                product,
                fcmType.getMessage(),
                fcmType.getRedirectUri().buildUri(product.getId().toString())
        );
    }

    private void notifyBuyers(ProductEntity product, FcmMessageType fcmType) {
        sendPushBuyerBidNotification(
                product,
                fcmType.getMessage(),
                fcmType.getRedirectUri().buildUri(product.getId().toString())
        );
    }

    // 채팅 알림 전송 요청
    private void sendPushChatNotification(String roomId, Long receiverId, String userNickname, String content, FcmMessageType fcmType) {
        fcmSender.sendPushChatMessageNotification(
                new FcmChatMessage(receiverId, userNickname, fcmType.getMessage(), content),
                        fcmType.getRedirectUri().buildUri(roomId, receiverId.toString())
        );
    }

    // 판매자에게 알림 전송 요청
    private void sendPushSellerBidNotification(ProductEntity productEntity, String titleMessage, String uri) {
        fcmSender.sendPushSellerBidNotification(
                productEntity.getUser().getId(), productEntity.getTitle(), titleMessage, uri
        );
    }

    // 구매자에게 알림 전송 요청
    private void sendPushBuyerBidNotification(ProductEntity product, String titleMessage, String uri){
        //경매에 참여한 userId들
        List<Long> userIds = auctionRepository.findAuctionEntitiesByProductId(product.getId())
                .stream()
                .map(auctionEntity -> auctionEntity.getUser().getId())
                .toList();

        fcmSender.sendPushBuyerBidNotification(userIds, product.getTitle(), titleMessage, uri);
    }
}
