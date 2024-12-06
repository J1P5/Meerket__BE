package org.j1p5.api.auction.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.PlaceBidResponse;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.api.fcm.FcmService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceBidUseCase {

    private final AuctionService auctionService;
    private final FcmService fcmService;

    public PlaceBidResponse execute(Long userId, Long productId, int price) {

        auctionService.checkDuplicateBid(userId, productId);

        PlaceBidResponse placeBidResponse = auctionService.placeBid(userId, productId, price);

        fcmService.sendSellerBidMessage(productId);

        return placeBidResponse;
    }

}
