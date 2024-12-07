package org.j1p5.api.auction.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.api.fcm.FcmService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelBidUseCase {

    private final AuctionService auctionService;
    private final FcmService fcmService;

    public void execute(Long userId, Long auctionId) {

        auctionService.verifyUserBidOwnership(userId, auctionId);

        Long productId = auctionService.cancelBid(auctionId);

        fcmService.sendSellerBidCancelMessage(productId);
    }
}
