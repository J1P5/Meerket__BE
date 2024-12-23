package org.j1p5.api.chat.dto.response;

import java.util.Objects;

public record CreateChatRoomResponse(String roomId) {
    public CreateChatRoomResponse {
        Objects.requireNonNull(roomId, "roomId는 필수입니다.");
    }
}
