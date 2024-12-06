package org.j1p5.api.chat.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatMessageRequest(
        @NotNull
        @Size(min = 24, max = 24, message = "roomId는 24자여야 합니다.")
        String roomId,

        @NotNull
        @Min(1)
        Long receiverId,

        @NotNull
        @Min(1)
        Long senderId,

        @NotBlank(message = "메시지는 공백일 수 없습니다.")
        @Size(max = 500, message = "메시지는 최대 500자까지 입력 가능합니다.")
        String content
) {
}
