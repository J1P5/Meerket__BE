package org.j1p5.domain.product.dto;

import org.j1p5.domain.product.entity.ProductEntity;

import java.time.LocalDateTime;

public record MyProductResponseInfo(
        String title,
        String imageUrl,
        String productAddress,//읍면동 주소가 들어가야하네
        LocalDateTime createdAt,
        int price,
        LocalDateTime expiredTime,
        Integer winningPrice

) {
    public static MyProductResponseInfo from(ProductEntity product){
        return new MyProductResponseInfo(
                product.getTitle(),
                product.getThumbnail(),
                product.getLocation(),
                product.getCreatedAt(),
                product.getMinPrice(),
                product.getExpiredTime(),
                product.getWinningPrice()
        );
    }
}
