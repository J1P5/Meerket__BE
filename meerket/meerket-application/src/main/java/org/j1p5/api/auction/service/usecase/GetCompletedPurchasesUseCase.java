package org.j1p5.api.auction.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.BidHistoryResponse;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCompletedPurchasesUseCase {

    private final AuctionService auctionService;

    public List<BidHistoryResponse> execute(Long userId) {

        List<AuctionEntity> entities = auctionService.getCompletedPurchasesByUserId(userId);

        return auctionService.getAuctionHistoryResponses(entities);

    }
}
