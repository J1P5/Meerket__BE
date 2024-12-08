package org.j1p5.api.comment.dto.response;

import org.j1p5.api.comment.dto.CommentMemeberDto;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.comment.entity.CommentStatus;
import org.j1p5.domain.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record CommentReadResponseDto(
        CommentMemeberDto commentMemeberDto,
        Long commentId,
        String content,
        boolean isSeller,//판매자인지
        boolean isUpdatable,//수정된 글인지
        LocalDateTime createdAt,
        List<CommentReadResponseDto> replies,
        CommentStatus status
) {
    public static CommentReadResponseDto of(CommentEntity commentEntity,UserEntity user){
        boolean isUpdated = commentEntity.getStatus().equals(CommentStatus.UPDATED);//수정여부 판단하기 위해
        return new CommentReadResponseDto(
                new CommentMemeberDto(user.getNickname(),user.getImageUrl()),
                commentEntity.getId(),
                commentEntity.getContent(),
                false,
                isUpdated,
                commentEntity.getCreatedAt(),
                commentEntity.getReplies().stream()
                        .map(reply -> CommentReadResponseDto.of(reply,reply.getUser()))
                        .collect(Collectors.toList()),
                commentEntity.getStatus()
        );
    }
}
