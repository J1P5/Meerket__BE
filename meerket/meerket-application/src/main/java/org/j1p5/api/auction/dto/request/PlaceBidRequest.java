package org.j1p5.api.auction.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PlaceBidRequest(@NotNull @Min(1) Long productId, @Min(0) int price) {}
