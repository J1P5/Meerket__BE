package org.j1p5.api.comment.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.comment.dto.request.CommentCreateRequestDto;
import org.j1p5.api.comment.dto.response.CommentReadResponseDto;
import org.j1p5.api.comment.service.CommentService;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.comment.entity.CommentStatus;
import org.j1p5.domain.comment.repository.CommentRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.service.UserReader;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentCreateUsecase {
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserReader userReader;

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

        commentRepository.save(comment);
    }

}