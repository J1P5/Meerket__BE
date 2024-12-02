package org.j1p5.api.product.dto.request;

import java.time.LocalDateTime;
import lombok.Builder;
import org.j1p5.domain.product.dto.ProductInfo;
import org.j1p5.domain.product.entity.ProductCategory;
import org.j1p5.domain.product.entity.ProductStatus;

@Builder
public record ProductCreateRequestDto(
        String title,
        String content,
        int price,
        ProductCategory category,
        Double latitude,
        Double longtitude,
        String address,
        String location,
        ProductStatus status,
        LocalDateTime expiredTime) {

    public static ProductInfo toInfo(ProductCreateRequestDto requestDto) {
        return ProductInfo.builder()
                .title(requestDto.title)
                .content(requestDto.content)
                .address(requestDto.address)
                .location(requestDto.location)
                .price(requestDto.price)
                .category(requestDto.category)
                .latitude(requestDto.latitude)
                .longtitude(requestDto.longtitude)
                .expiredTime(requestDto.expiredTime)
                .status(requestDto.status)
                .build();
    }
}
