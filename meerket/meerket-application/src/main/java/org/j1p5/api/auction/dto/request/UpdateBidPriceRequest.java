package org.j1p5.api.auction.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateBidPriceRequest(

    @NotNull
    @Min(1)
    Long auctionId,

    @NotNull
    @Min(1)
    Long productId,

    int price

) {
}
