package org.j1p5.api.chat.dto;


import java.util.Objects;

public record ChatRoomBasicInfo(
        String roomId,
        String otherNickname,
        String otherProfileImage,
        Long otherUserId,
        Long productId,
        String productTitle,
        String productImage,
        int price,
        boolean isSeller

) {
    public ChatRoomBasicInfo{
        Objects.requireNonNull(roomId,"roomId는 필수입니다.");
        Objects.requireNonNull(otherNickname,"otherNickname은 필수입니다.");
        Objects.requireNonNull(otherUserId,"otherUserId는 필수입니다.");
        Objects.requireNonNull(productId,"productId는 필수입니다.");
        Objects.requireNonNull(productTitle,"productTitle은 필수입니다.");
    }
}
