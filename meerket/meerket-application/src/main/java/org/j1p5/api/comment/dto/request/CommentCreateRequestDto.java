package org.j1p5.api.comment.dto.request;

public record CommentCreateRequestDto(Long parentId, String content) {}
