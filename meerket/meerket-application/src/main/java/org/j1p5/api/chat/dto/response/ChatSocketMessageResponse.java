package org.j1p5.api.chat.dto.response;

import org.j1p5.domain.chat.entity.ChatMessageEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public record ChatSocketMessageResponse(
        String id,
        Long senderId,
        String content,
        LocalDateTime createdAt
) {
    public ChatSocketMessageResponse {
        Objects.requireNonNull(senderId, "senderId는 필수입니다.");
        Objects.requireNonNull(content, "content는 필수입니다.");
        Objects.requireNonNull(id, "id는 필수입니다.");
        Objects.requireNonNull(createdAt, "createdAt은 필수입니다.");
    }

    public static ChatSocketMessageResponse fromEntity(ChatMessageEntity chatMessageEntity) {
        return new ChatSocketMessageResponse(
                chatMessageEntity.getId().toString(),
                chatMessageEntity.getSenderId(),
                chatMessageEntity.getContent(),
                chatMessageEntity.getCreatedAt()
        );
    }

}
