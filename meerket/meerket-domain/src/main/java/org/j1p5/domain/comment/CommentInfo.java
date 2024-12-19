package org.j1p5.domain.comment;

import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.comment.entity.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record CommentInfo(
        Long userId,
        String userNickname,
        String userProfileImage,
        Long commentId,
        String content,
        boolean isWithdrawUser, //탈퇴한 사용자인지
        boolean isBlocked,//차단된 댓글인지
        boolean isSeller,//판매자인지
        boolean isUpdatable,//수정된 글인지
        LocalDateTime createdAt,
        List<CommentInfo> replies,
        CommentStatus status
) {
    public static CommentInfo of(CommentEntity commentEntity, List<Long> blockUserIds, Long sellerId) {
        boolean isSeller = commentEntity.getProduct().getUser().getId().equals(sellerId);
        boolean isUpdated = commentEntity.getStatus().equals(CommentStatus.UPDATED);//수정여부 판단하기 위해
        boolean isBlocked = blockUserIds.contains(commentEntity.getUser().getId());
        boolean isWithdrawUser = commentEntity.getUser().isDeleted();

        return new CommentInfo(
                commentEntity.getUser().getId(),
                commentEntity.getUser().getNickname(),
                commentEntity.getUser().getImageUrl(),
                commentEntity.getId(),
                commentEntity.getContent(),
                isWithdrawUser,
                isBlocked,
                isSeller,
                isUpdated,
                commentEntity.getCreatedAt(),
                commentEntity.getReplies().stream()
                        .map(c -> CommentInfo.of(c, blockUserIds, sellerId))
                        .collect(Collectors.toList()),
                commentEntity.getStatus()
        );
    }
}
