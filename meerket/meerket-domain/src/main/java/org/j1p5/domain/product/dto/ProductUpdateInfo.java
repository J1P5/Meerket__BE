package org.j1p5.domain.product.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.j1p5.domain.product.entity.ProductCategory;

@Builder
public record ProductUpdateInfo(
        String title,
        String content,
        Integer minimumPrice,
        ProductCategory category,
        LocalDateTime expiredTime,
        Double latitude,
        Double longtitude,
        String address,
        String location
) {}
