package org.j1p5.api.product.dto.request;

import lombok.Builder;
import org.j1p5.domain.product.dto.ProductInfo;
import org.j1p5.domain.product.entity.ProductCategory;

import java.time.LocalDateTime;

@Builder
public record ProductCreateRequestDto(
        String title,
        String content,
        int minimumPrice,
        ProductCategory category,
        Double latitude,
        Double longitude,
        String address,
        String location,
        LocalDateTime expiredTime) {

    public static ProductInfo toInfo(ProductCreateRequestDto requestDto) {
        return ProductInfo.builder()
                .title(requestDto.title)
                .content(requestDto.content)
                .address(requestDto.address)
                .location(requestDto.location)
                .price(requestDto.minimumPrice)
                .category(requestDto.category)
                .latitude(requestDto.latitude)
                .longtitude(requestDto.longitude)
                .expiredTime(requestDto.expiredTime)
                .build();
    }
}
