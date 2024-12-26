package org.j1p5.api.auction.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.PlaceBidResponse;
import org.j1p5.api.auction.exception.AuctionException;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.j1p5.domain.global.exception.DomainException;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.j1p5.infrastructure.global.exception.InfraException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.j1p5.api.auction.exception.AuctionException.SELLER_CANNOT_CREATE_BID;
import static org.j1p5.api.product.exception.ProductException.PRODUCT_NOT_FOUND;
import static org.j1p5.domain.global.exception.DomainErrorCode.USER_NOT_FOUND;
import static org.j1p5.infrastructure.fcm.exception.FcmException.CREATE_BID_FCM_ERROR;

@Service
@RequiredArgsConstructor
public class PlaceBidUseCase {

    private final AuctionRepository auctionRepository;
    private final FcmService fcmService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * 입찰 후 알림 전송
     * 판매자는 입찰 불가, 중복 입찰 불가
     * @author sunghyun, yechan
     * @param userId
     * @param productId
     * @param price
     * @return
     */
    @Transactional
    public PlaceBidResponse execute(Long userId, Long productId, int price) {

        //판매자가 자기꺼 입찰하는경우 막아야함
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(PRODUCT_NOT_FOUND));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(USER_NOT_FOUND));

        if(product.getUser().equals(user)){
            throw new DomainException(SELLER_CANNOT_CREATE_BID);
        }

        checkDuplicateBid(userId, productId);

        PlaceBidResponse placeBidResponse = placeBid(userId, productId, price);

        product.updateHasBuyer();
        //productRepository.save(product);

        try {
            fcmService.sendSellerBidMessage(productId);
        } catch (Exception e) {
            throw new InfraException(CREATE_BID_FCM_ERROR);
        }
        return placeBidResponse;
    }


    private PlaceBidResponse placeBid(Long userId, Long productId, int price) {

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new WebException(PRODUCT_NOT_FOUND));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new WebException(AuctionException.BID_USER_NOT_FOUND));

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

}
