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
    public void updateComment(Long productId, Long commentId, Long userId, CommentUpdateRequestDto request) {
        UserEntity user = commentService.getUser(userId);
        ProductEntity product = commentService.getProduct(productId);
        CommentEntity comment = commentService.getComment(commentId);


        commentService.validateCommentUpdate(product, comment, user, request);

    }
}