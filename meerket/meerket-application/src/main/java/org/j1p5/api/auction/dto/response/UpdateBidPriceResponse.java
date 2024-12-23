package org.j1p5.api.auction.dto.response;

import java.util.Objects;
import org.j1p5.domain.auction.entity.AuctionEntity;

public record UpdateBidPriceResponse(Long auctionId, int price) {
    public UpdateBidPriceResponse {
        Objects.requireNonNull(auctionId, "auctionId는 필수입니다.");
    }

    public static UpdateBidPriceResponse fromEntity(AuctionEntity auctionEntity) {
        return new UpdateBidPriceResponse(auctionEntity.getId(), auctionEntity.getPrice());
    }
}
