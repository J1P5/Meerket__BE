package org.j1p5.api.chat.dto.response;

import org.j1p5.domain.chat.entity.ChatMessageEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public record ChatMessageResponse(
        String id,
        Long senderId,
        String content,
        LocalDateTime createdAt
) {

    public static ChatMessageResponse fromEntity(ChatMessageEntity entity) {
        return new ChatMessageResponse(
                entity.getId().toString(),
                entity.getSenderId(),
                entity.getContent(),
                entity.getCreatedAt());
    }

    public ChatMessageResponse{
        Objects.requireNonNull(id, "id는 필수값입니다.");
        Objects.requireNonNull(senderId, "senderId는 필수값입니다.");
        Objects.requireNonNull(content, "내용은 필수 값입니다.");
        Objects.requireNonNull(createdAt, "생성시간은 필수값입니다.");
    }
}
