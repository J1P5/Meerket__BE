package org.j1p5.api.product.dto.response;

import org.j1p5.domain.product.entity.ProductEntity;

public record CreateProductResponseDto(
        Long productId
) {
    public static CreateProductResponseDto from(ProductEntity product){
        return new CreateProductResponseDto(product.getId());
    }
}
