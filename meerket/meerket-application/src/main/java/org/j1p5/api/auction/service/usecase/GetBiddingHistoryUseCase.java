package org.j1p5.api.auction.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.GetBiddingHistoryResponse;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBiddingHistoryUseCase {

    private final AuctionService auctionService;

    public List<GetBiddingHistoryResponse> execute(Long userId) {

        List<AuctionEntity> biddingAuctionsEntities = auctionService.getBiddingAuctionsByUserId(userId);

        return auctionService.getAuctionHistoryResponses(biddingAuctionsEntities);

    }
}
