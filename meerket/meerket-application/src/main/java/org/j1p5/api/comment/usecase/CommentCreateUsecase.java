package org.j1p5.api.comment.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.comment.dto.request.CommentCreateRequestDto;
import org.j1p5.api.comment.service.CommentService;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.comment.entity.CommentStatus;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentCreateUsecase {
    private final CommentService commentService;


    /**
     * 댓글 생성. 대댓글 depth는 1개 까지만
     * @author sunghyun
     * @param productId
     * @param userId
     * @param request
     */
    @Transactional
    public void createComment(Long productId, Long userId, CommentCreateRequestDto request) {

        ProductEntity product = commentService.getProduct(productId);

        UserEntity user = commentService.getUser(userId);

        CommentEntity parentComment = commentService.validateParentComment(request.parentId());

        CommentEntity comment = CommentEntity.builder()
                .content(request.content())
                .parentComment(parentComment)
                .status(CommentStatus.ACTIVE)
                .product(product)
                .user(user)
                .build();

        commentService.appendComment(comment);
    }

}