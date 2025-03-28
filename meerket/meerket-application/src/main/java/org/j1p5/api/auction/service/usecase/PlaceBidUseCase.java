package org.j1p5.api.auction.service.usecase;

import static org.j1p5.api.auction.exception.AuctionException.SELLER_CANNOT_CREATE_BID;
import static org.j1p5.api.product.exception.ProductException.PRODUCT_NOT_FOUND;
import static org.j1p5.domain.global.exception.DomainErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.api.auction.dto.response.PlaceBidResponse;
import org.j1p5.api.auction.exception.AuctionException;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.j1p5.domain.global.exception.DomainException;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.redis.RedisBidLockService;
import org.j1p5.domain.redis.RedisIdempotencyService;
import org.j1p5.domain.redis.RedisProductEditLockService;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceBidUseCase {

    private final AuctionRepository auctionRepository;
    private final FcmService fcmService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RedisBidLockService redisBidLockService;
    private final RedisIdempotencyService redisIdempotencyService;
    private final RedisProductEditLockService redisProductEditLockService;

    private static final long REQUEST_ID_TTL = 30;
    private static final long BID_LOCK_TTL = 300;

    /**
     * 입찰 후 알림 전송 판매자는 입찰 불가, 중복 입찰 불가
     *
     * @author sunghyun, yechan
     * @param userId
     * @param productId
     * @param price
     * @return
     */
    @Transactional
    public PlaceBidResponse execute(Long userId, Long productId, int price) {
        // TODO 사용자의 거리를 기반하여 물건의 위치와 얼마나 떨어져있는지 확인해야 함.

        // 멱등성 확인
        String requestId = "product:" + productId + ":user:" + userId;
        if (!redisIdempotencyService.saveRequestId(requestId, REQUEST_ID_TTL)) {
            throw new WebException(AuctionException.DUPLICATE_BID_REQUEST);
        }

        // 상품 수정중인지 확인
        String editLockKey = "product:" + productId + ":lock:edit";
        if (redisProductEditLockService.isEditLocked(editLockKey)) {
            throw new WebException(AuctionException.AUCTION_EDIT_IN_PROGRESS);
        }

        // 입찰 중 락 설정
        String bidLockKey = "product:" + productId + ":lock:bid";

        try {
            redisBidLockService.incrementBid(bidLockKey, BID_LOCK_TTL);

            ProductEntity product = getProductEntity(productId);
            UserEntity user = getUserEntity(userId);

            verifySeller(product, user);

            checkDuplicateBid(userId, productId);

            PlaceBidResponse placeBidResponse = placeBid(userId, productId, price);

            product.updateHasBuyer();

            sendSellerBidNotification(productId);

            return placeBidResponse;
        } finally {
            try {
                redisBidLockService.decrementBid(bidLockKey);
            } catch (Exception e) {
                log.error("입찰 락을 해제하는데 실패했습니다.", e);
            }
        }
    }

    private static void verifySeller(ProductEntity product, UserEntity user) {
        if (product.getUser().equals(user)) {
            throw new DomainException(SELLER_CANNOT_CREATE_BID);
        }
    }

    private UserEntity getUserEntity(Long userId) {
        UserEntity user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new DomainException(USER_NOT_FOUND));
        return user;
    }

    private ProductEntity getProductEntity(Long productId) {
        ProductEntity product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() -> new DomainException(PRODUCT_NOT_FOUND));
        return product;
    }

    private PlaceBidResponse placeBid(Long userId, Long productId, int price) {

        ProductEntity productEntity = getProductEntity(productId);

        UserEntity userEntity = getUserEntity(userId);

        if (productEntity.getMinPrice() > price) {
            throw new WebException(AuctionException.AUCTION_MIN_PRICE_ERROR);
        }

        AuctionEntity auctionEntity = AuctionEntity.create(userEntity, productEntity, price);

        auctionRepository.save(auctionEntity);

        return PlaceBidResponse.fromEntity(auctionEntity);
    }

    private void checkDuplicateBid(Long userId, Long productId) {
        boolean exists = auctionRepository.existsByUserIdAndProductId(userId, productId);
        if (exists) {
            throw new WebException(AuctionException.DUPLICATE_BID);
        }
    }

    private void sendSellerBidNotification(Long productId) {
        try {
            fcmService.sendSellerBidMessage(productId);
        } catch (Exception e) {
            log.error("판매자에게 FCM 알림을 보내지 못했습니다.", e);
        }
    }
}
