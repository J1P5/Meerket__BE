package org.j1p5.domain.product.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.j1p5.domain.product.entity.ProductEntity;

@Builder
public record ProductResponseInfo(
        String myLocation, // 일단 활동지역은 1개만
        long productId,
        long memberId,
        String title,
        int price,
        String address,
        LocalDateTime uploadTime,
        LocalDateTime expiredTime,
        boolean isEarly,
        String image) {

    public static ProductResponseInfo from(ProductEntity product, MyLocationInfo myLocationInfo) {

        ProductResponseInfo productResponseInfos =
                ProductResponseInfo.builder()
                        .myLocation(myLocationInfo.address()) // 사용자 활동지역
                        .productId(product.getId())
                        .memberId(product.getUser().getId())
                        .title(product.getTitle())
                        .price(product.getMinPrice())
                        .address(product.getAddress()) // 물건의 주소(거래 희망장소)
                        .uploadTime(product.getCreatedAt())
                        .expiredTime(product.getExpiredTime())
                        .isEarly(product.isEarly())
                        .image(
                                product.getImageEntityList().isEmpty()
                                        ? null
                                        : product.getImageEntityList()
                                                .get(0)
                                                .getImageUrl()) // 대표 이미지 한 개
                        .build();

        return productResponseInfos;
    }
}
