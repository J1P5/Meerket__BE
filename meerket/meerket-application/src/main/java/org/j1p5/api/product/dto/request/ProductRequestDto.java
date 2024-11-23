package org.j1p5.api.product.dto.request;

import org.j1p5.domain.product.entity.ProductCategory;

import java.time.LocalDateTime;

public record ProductRequestDto(
        String title,
        String content,
        int price,
        ProductCategory category,
        Double latitude,
        Double longtitude,
        String address,
        String location,
        LocalDateTime expiredTime
) {
}
