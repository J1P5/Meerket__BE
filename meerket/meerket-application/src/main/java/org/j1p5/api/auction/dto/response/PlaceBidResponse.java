package org.j1p5.api.auction.dto.response;

import org.j1p5.domain.auction.entity.AuctionEntity;

import java.util.Objects;

public record PlaceBidResponse(
        Long auctionId,
        int price
) {
    public PlaceBidResponse {
        Objects.requireNonNull(auctionId, "id는 필수입니다.");
        Objects.requireNonNull(price, "price는 필수입니다.");
    }

    public static PlaceBidResponse fromEntity(AuctionEntity entity) {
        return new PlaceBidResponse(
                entity.getId(),
                entity.getPrice()
        );
    }
}
