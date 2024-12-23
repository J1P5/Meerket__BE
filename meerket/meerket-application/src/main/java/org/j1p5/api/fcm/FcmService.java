package org.j1p5.api.fcm;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.chat.exception.ChatException;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.api.product.exception.ProductException;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.repository.AuctionRepository;
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
    private final AuctionRepository auctionRepository;

    private static final String BID_ALERT_MESSAGE = "상품에 누군가 입찰했어요!";
    private static final String BID_UPDATE_MESSAGE = "상품에 입찰금액 변동이 발생했어요";
    private static final String BID_CANCEL_MESSAGE = "상품에 입찰이 취소되었어요";
    private static final String BID_EARLY_CLOSED_MESSAGE = "판매자가 판매 조기마감을 하여 2시간뒤에 입찰이 마감됩니다";
    private static final String BID_DELETED_MESSAGE = "판매자가 게시물을 삭제하였습니다. 해당 입찰은 유효하지 않습니다.";

    // 채팅 알림
    public void sendFcmChatMessage(String roomId, Long receiverId, Long userId, String content) {
        UserEntity userEntity =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new InfraException(ChatException.CHAT_RECEIVER_NOT_FOUND));

        fcmSender.sendPushChatMessageNotification(
                roomId, receiverId, userEntity.getNickname(), content);
    }

    // 판매자에게 입찰 알림
    public void sendSellerBidMessage(Long productId) {

        ProductEntity productEntity = getProductEntity(productId);

        sendPushSellerBidNotification(productEntity, BID_ALERT_MESSAGE);
    }

    // 판매자에게 입찰 수정 알림
    public void sendSellerBidUpdateMessage(Long productId) {
        ProductEntity productEntity = getProductEntity(productId);

        sendPushSellerBidNotification(productEntity, BID_UPDATE_MESSAGE);
    }

    // 판매자에게 입찰 취소 알림
    public void sendSellerBidCancelMessage(Long productId) {
        ProductEntity productEntity = getProductEntity(productId);

        sendPushSellerBidNotification(productEntity, BID_CANCEL_MESSAGE);
    }

    // 판매자에게 알림 전송 요청
    private void sendPushSellerBidNotification(ProductEntity productEntity, String content) {
        fcmSender.sendPushSellerBidNotification(
                productEntity.getId(),
                productEntity.getUser().getId(),
                productEntity.getTitle(),
                content);
    }

    private ProductEntity getProductEntity(Long productId) {
        ProductEntity productEntity =
                productRepository
                        .findById(productId)
                        .orElseThrow(() -> new WebException(ProductException.PRODUCT_NOT_FOUND));
        return productEntity;
    }

    // 사용자 로그인 했을때 fcm 토큰 추가 or 업데이트
    public void saveFcmToken(Long userId, String fcmToken) {
        // TODO 사용자 찾지못했을때의 커스텀 예외 처리
        UserEntity userEntity =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        fcmTokenRepository
                .findByUserId(userId)
                .ifPresentOrElse(
                        existingToken -> {
                            if (existingToken.getToken().equals(fcmToken)) return;

                            existingToken.updateToken(fcmToken);
                            fcmTokenRepository.save(existingToken);
                        },
                        () -> {
                            fcmTokenRepository.save(FcmTokenEntity.create(userEntity, fcmToken));
                        });
    }

    // 조기마감 시 구매자에게 알림
    public void sendBuyerCloseEarlyMessage(Long productId) {
        ProductEntity product = this.getProductEntity(productId);
        String content = BID_EARLY_CLOSED_MESSAGE;
        sendPushBuyerBidNotification(product, content);
    }

    // 상품 삭제되었을 때 구매자에게 알림
    public void sendBuyerProductDeleted(Long productId) {
        ProductEntity product = this.getProductEntity(productId);
        String content = BID_DELETED_MESSAGE;
        sendPushBuyerBidNotification(product, content);
    }

    // 구매자에게 알림 전송 요청
    private void sendPushBuyerBidNotification(ProductEntity product, String content) {
        List<AuctionEntity> auctionEntities =
                auctionRepository.findAuctionEntitiesByProductId(product.getId());
        List<Long> userIds = new ArrayList<>();
        for (AuctionEntity auction : auctionEntities) {
            userIds.add(auction.getUser().getId());
        } // 경매애 참여한 userId들

        fcmSender.sendPushBuyerBidNotification(
                product.getId(), userIds, product.getTitle(), content);
    }
}
