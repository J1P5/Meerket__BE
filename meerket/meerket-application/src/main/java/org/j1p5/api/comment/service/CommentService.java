package org.j1p5.api.comment.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.comment.dto.request.CommentUpdateRequestDto;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.comment.repository.CommentRepository;
import org.j1p5.domain.global.exception.DomainException;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.service.UserReader;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.j1p5.api.comment.exception.CommentErrorCode.*;
import static org.j1p5.domain.product.exception.ProductException.PRODUCT_HAS_BUYER;
import static org.j1p5.domain.product.exception.ProductException.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserReader userReader;

    public CommentEntity validateParentComment(Long parentCommentId) {
        Long parentId = parentCommentId;
        CommentEntity parentComment = null;
        if (parentId != null) { //대댓글 구현 로직
            parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new WebException(COMMENT_NOT_FOUND));

            if (parentComment.getParentComment() != null) {// 대대댓글 제한 로직, depth1로 제한
                throw new WebException(COMMENT_DEPTH_EXCEEDED);
            }
        }
        return parentComment;
    }

    public ProductEntity getProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(PRODUCT_NOT_FOUND));
        return product;
    }

    public UserEntity getUser(Long userId) {
        UserEntity user = userReader.getById(userId);
        return user;
    }

    public CommentEntity getComment(Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new WebException(COMMENT_NOT_FOUND));
        return comment;
    }

    public List<CommentEntity> getComments(Long productId, Pageable pageable) {
        return commentRepository.findParentCommentByProductId(productId, pageable);
    }

    public void validateCommentUpdate(Long commentId, Long userId,CommentUpdateRequestDto request) {

        ProductEntity product = this.getProduct(request.productId());
        UserEntity user = this.getUser(userId);
        CommentEntity comment = this.getComment(commentId);

        if (product.isHasBuyer()) {
            throw new WebException(PRODUCT_HAS_BUYER);
        }
        if (!comment.getUser().equals(user)) throw new WebException(COMMENT_NOT_AUTHORIZED);

        comment.updateContent(request.content());
    }

    public void removedComment(Long productId, Long userId, Long commentId) {
        ProductEntity product = this.getProduct(productId);
        UserEntity user = this.getUser(userId);
        CommentEntity comment = this.getComment(commentId);

        if (product.isHasBuyer()) throw new WebException(PRODUCT_HAS_BUYER);

        if (!comment.getUser().equals(user)) throw new WebException(COMMENT_NOT_AUTHORIZED);
        comment.updateStatusDelete();
    }


}