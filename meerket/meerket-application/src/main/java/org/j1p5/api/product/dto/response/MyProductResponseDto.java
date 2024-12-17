package org.j1p5.api.product.dto.response;

import org.j1p5.api.product.service.EmdNameReader;
import org.j1p5.domain.product.dto.MyProductResponseInfo;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.entity.ProductStatus;

import java.time.LocalDateTime;

public record MyProductResponseDto(
        Long productId,
        String title,
        String imageUrl,
        String productAddress,//읍면동 주소가 들어가야하네
        LocalDateTime createdAt,
        int price,
        LocalDateTime expiredTime,
        Integer winningPrice,
        ProductStatus status//이걸로 마이페이지 판매중/판매완료 확인 가능

) {
    public static MyProductResponseDto from(ProductEntity product){
        return new MyProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getThumbnail(),
                EmdNameReader.getEmdName(product.getUser()),
                product.getCreatedAt(),
                product.getMinPrice(),
                product.getExpiredTime(),
                product.getWinningPrice(),
                product.getStatus()
        );
    }
}
