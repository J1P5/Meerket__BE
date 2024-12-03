package org.j1p5.api.chat.dto.response;

import org.j1p5.api.chat.dto.ChatRoomBasicInfo;

import java.util.List;
import java.util.Objects;

public record ChatRoomEnterResponse(
        ChatRoomBasicInfo chatRoomBasicInfo,
        List<ChatMessageResponse> messages
) {
    public ChatRoomEnterResponse{
        Objects.requireNonNull(chatRoomBasicInfo, "채팅방 기본정보는 필수입니다.");
        Objects.requireNonNull(messages, "메시지 리스트는 필수입니다.");
    }
}
