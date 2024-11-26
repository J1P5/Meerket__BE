package org.j1p5.api.product.dto.request;

import lombok.Builder;
import org.j1p5.domain.product.dto.ProductInfo;
import org.j1p5.domain.product.entity.ProductCategory;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.user.entity.UserEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.time.LocalDateTime;

@Builder
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

    public static ProductInfo toInfo(ProductRequestDto requestDto){
        return ProductInfo.builder()
                .title(requestDto.title)
                .content(requestDto.content)
                .address(requestDto.address)
                .location(requestDto.location)
                .price(requestDto.price)
                .category(requestDto.category)
                .latitude(requestDto.latitude)
                .longtitude(requestDto.longtitude())
                .expiredTime(requestDto.expiredTime)
                .build();
    }

}
