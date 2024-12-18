package org.j1p5.api.comment.dto;

public record CommentMemeberDto(
        Long userId,
        String nickname,
        String profileIamge
) {
}
