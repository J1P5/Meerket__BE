package org.j1p5.api.auction.service.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.BidHistoryResponse;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCompletedPurchasesUseCase {

    private final AuctionService auctionService;

    /**
     * 거래 완료 입찰 내역 조회
     *
     * @author yechan
     * @param userId
     * @return 입찰내역 리스트
     */
    public List<BidHistoryResponse> execute(Long userId) {

        List<AuctionEntity> entities = auctionService.getCompletedPurchasesByUserId(userId);

        return auctionService.getAuctionHistoryResponses(entities);
    }
}
