package org.j1p5.api.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.j1p5.api.comment.dto.CommentMemeberDto;
import org.j1p5.domain.comment.CommentInfo;
import org.j1p5.domain.comment.entity.CommentStatus;

public record CommentReadResponseDto(
        CommentMemeberDto commentMemeberDto,
        Long commentId,
        String content,
        boolean isWithdrawUser,
        boolean isBlocked,
        boolean isSeller, // 판매자인지
        boolean isUpdatable, // 수정된 글인지
        LocalDateTime createdAt,
        List<CommentReadResponseDto> replies,
        CommentStatus status) {
    public static CommentReadResponseDto of(CommentInfo commentInfo) {
        return new CommentReadResponseDto(
                new CommentMemeberDto(
                        commentInfo.userId(),
                        commentInfo.userNickname(),
                        commentInfo.userProfileImage()),
                commentInfo.commentId(),
                commentInfo.content(),
                commentInfo.isWithdrawUser(),
                commentInfo.isBlocked(),
                commentInfo.isSeller(),
                commentInfo.isUpdatable(),
                commentInfo.createdAt(),
                commentInfo.replies().stream()
                        .map(CommentReadResponseDto::of)
                        .collect(Collectors.toList()),
                commentInfo.status());
    }
}
