package org.j1p5.domain.chat.command;

import java.time.LocalDateTime;
import java.util.Objects;
import org.bson.types.ObjectId;

public record UpdateChatRoomCommand(
        ObjectId roomId,
        String content,
        Long receiverId,
        boolean receiverInChatRoom,
        LocalDateTime createdAt) {

    public UpdateChatRoomCommand {
        Objects.requireNonNull(roomId, "roomId 는 필수입니다.");
        Objects.requireNonNull(content, "content 는 필수입니다.");
        Objects.requireNonNull(receiverId, "receiverId 는 필수입니다.");
        Objects.requireNonNull(createdAt, "createdAt 는 필수입니다.");
    }
}
