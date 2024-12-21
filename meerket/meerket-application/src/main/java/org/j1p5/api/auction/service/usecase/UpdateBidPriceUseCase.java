package org.j1p5.api.auction.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.UpdateBidPriceResponse;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateBidPriceUseCase {

    private final AuctionService auctionService;
    private final FcmService fcmService;

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
            auctionEntity = auctionService.updateBidPriceForEarlyClosure(auctionId,price);
        }else{
            auctionEntity = auctionService.updateBidPriceWithMinimumLimit(auctionId,productId,price);
        }

        fcmService.sendSellerBidUpdateMessage(productId);

        return UpdateBidPriceResponse.fromEntity(auctionEntity);
    }
}
