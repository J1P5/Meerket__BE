package org.j1p5.api.product.dto.request;

import org.j1p5.domain.product.dto.ProductUpdateInfo;
import org.j1p5.domain.product.entity.ProductCategory;

import java.time.LocalDateTime;

public record ProductUpdateRequest(
        String title,
        String content,
        Integer minimumPrice,
        ProductCategory category,
        LocalDateTime expiredTime,
        Double latitude,
        Double longtitude,
        String address,
        String location
)
{

    public static ProductUpdateInfo toInfo(ProductUpdateRequest request) {
        return new ProductUpdateInfo(
                request.title,
                request.content,
                request.minimumPrice,
                request.category,
                request.expiredTime,
                request.latitude,
                request.longtitude,
                request.address,
                request.location
        );
    }
}
