package org.j1p5.api.chat.dto.response;

import java.time.LocalDateTime;
import java.util.Objects;

public record ChatRoomInfoResponse(
        String roomId,
        String lastMessage,
        LocalDateTime lastMessageAt,
        Long productId,
        int unreadCount,
        String productImage,
        String otherNickname,
        String otherProfileImage
) {

    public ChatRoomInfoResponse{
        Objects.requireNonNull(roomId, "roomId는 필수입니다.");
        Objects.requireNonNull(lastMessage, "lastMessage는 필수입니다.");
        Objects.requireNonNull(lastMessageAt, "lastMessageAt는 필수입니다.");
        Objects.requireNonNull(productId, "productId는 필수입니다.");
        Objects.requireNonNull(otherNickname, "otherNickname는 필수입니다.");
    }
}
