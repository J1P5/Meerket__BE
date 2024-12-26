package org.j1p5.api.auction.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.UpdateBidPriceResponse;
import org.j1p5.api.auction.exception.AuctionException;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.api.product.exception.ProductException;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateBidPriceUseCase {

    private final AuctionService auctionService;
    private final AuctionRepository auctionRepository;
    private final FcmService fcmService;
    private final ProductRepository productRepository;

    /**
     * 입찰 수정. 본인 입찰만 수정 가능, 조기마감 여부에 따라 수정 범위 제한
     * 입찰 수정 알림 전송
     * @author yechan
     * @param productId
     * @param userId
     * @param auctionId
     * @param price
     * @return auction Id, 가격
     */
    @Transactional
    public UpdateBidPriceResponse execute(Long productId, Long userId, Long auctionId, int price) {

        auctionService.verifyUserBidOwnership(userId, auctionId);

        boolean earlyClosure = auctionService.isEarlyClosure(productId);

        AuctionEntity auctionEntity;

        if (earlyClosure) {
            auctionEntity = updateBidPriceForEarlyClosure(auctionId,price);
        }else{
            auctionEntity = updateBidPriceWithMinimumLimit(auctionId,productId,price);
        }

        fcmService.sendSellerBidUpdateMessage(productId);

        return UpdateBidPriceResponse.fromEntity(auctionEntity);
    }

    // 조기마감 상태일때의 수정
    private AuctionEntity updateBidPriceForEarlyClosure(Long auctionId, int price) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new WebException(AuctionException.BID_NOT_FOUND));

        if (auctionEntity.getPrice() >= price) {
            throw new WebException(AuctionException.BID_AMOUNT_TOO_LOW);
        }

        auctionEntity.updatePrice(price);
        auctionRepository.save(auctionEntity);
        return auctionEntity;
    }

    // 조기마감 상태가 아닐때의 수정
    public AuctionEntity updateBidPriceWithMinimumLimit(Long auctionId, Long productId, int price) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new WebException(ProductException.PRODUCT_NOT_FOUND));

        if (productEntity.getMinPrice() > price) {
            throw new WebException(AuctionException.AUCTION_MIN_PRICE_ERROR);
        }

        AuctionEntity auctionEntity = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new WebException(AuctionException.BID_NOT_FOUND));

        auctionEntity.updatePrice(price);
        auctionRepository.save(auctionEntity);
        return auctionEntity;
    }

}
