package org.j1p5.api.auction.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.BidHistoryResponse;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBiddingHistoryUseCase {

    private final AuctionService auctionService;
    private final AuctionRepository auctionRepository;

    /**
     * 입찰 내역 조회
     * @author yechan
     * @param userId
     * @return 입찰 내역 리스트
     */
    public List<BidHistoryResponse> execute(Long userId) {

        List<AuctionEntity> entities = getBiddingAuctionsByUserId(userId);

        return auctionService.getAuctionHistoryResponses(entities);

    }

    // 입찰 중인 기록 조회
    private List<AuctionEntity> getBiddingAuctionsByUserId(Long userId) {
        return auctionRepository.findBiddingAuctionsByUserId(userId);
    }
}
