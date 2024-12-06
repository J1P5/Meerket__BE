package org.j1p5.api.product.dto.response;

public record CloseEarlyResponseDto(
        Long productId //조기마감후 다시 상세페이지로 리다이렉트 할 수 있게 제공합니다
) {
    public static CloseEarlyResponseDto of(Long productId){
        return new CloseEarlyResponseDto(
                productId
        );
    }
}
