package org.j1p5.api.comment.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.comment.dto.request.CommentUpdateRequestDto;
import org.j1p5.api.comment.service.CommentService;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentUpdateUsecase {
    private final CommentService commentService;

    @Transactional
    public void updateComment(Long commentId, Long userId, CommentUpdateRequestDto request) {

        commentService.validateCommentUpdate(commentId, userId, request);

    }
}