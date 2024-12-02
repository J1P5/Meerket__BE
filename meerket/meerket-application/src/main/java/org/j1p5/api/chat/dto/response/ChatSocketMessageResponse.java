package org.j1p5.api.chat.dto.response;

import java.util.Objects;

public record ChatSocketMessageResponse(
        Long senderId,
        String content
) {
    public ChatSocketMessageResponse {
        Objects.requireNonNull(senderId, "senderId는 필수입니다.");
        Objects.requireNonNull(content, "content는 필수입니다.");
    }
}
