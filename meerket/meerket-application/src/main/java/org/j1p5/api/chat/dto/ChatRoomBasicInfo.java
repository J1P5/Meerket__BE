package org.j1p5.api.chat.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import org.j1p5.domain.product.entity.ProductStatus;

public record ChatRoomBasicInfo(
        String roomId,
        String otherNickname,
        String otherProfileImage,
        Long otherUserId,
        Long productId,
        String productTitle,
        String productImage,
        int price,
        boolean isSeller,
        boolean isChatAvailable,
        String sellerAddress,
        LocalDateTime productCreatedAt,
        ProductStatus productStatus) {
    public ChatRoomBasicInfo {
        Objects.requireNonNull(roomId, "roomId는 필수입니다.");
        Objects.requireNonNull(otherNickname, "otherNickname은 필수입니다.");
        Objects.requireNonNull(otherUserId, "otherUserId는 필수입니다.");
        Objects.requireNonNull(productId, "productId는 필수입니다.");
        Objects.requireNonNull(productTitle, "productTitle은 필수입니다.");
        Objects.requireNonNull(isChatAvailable, "isChatAvailable은 필수입니다.");
        Objects.requireNonNull(sellerAddress, "sellerAddress 는 필수입니다.");
        Objects.requireNonNull(productCreatedAt, "productCreatedAt 은 필수입니다.");
        Objects.requireNonNull(productStatus, "productStatus는 필수입니다.");
    }
}
