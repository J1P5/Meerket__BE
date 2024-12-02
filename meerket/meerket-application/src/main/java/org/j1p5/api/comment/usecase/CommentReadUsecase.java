package org.j1p5.api.comment.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.comment.dto.response.CommentReadResponseDto;
import org.j1p5.api.comment.service.CommentService;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentReadUsecase {
    private final CommentService commentService;


    public List<CommentReadResponseDto> getAllComments(Long productId, Long userId, Pageable pageable){


        ProductEntity product = commentService.getProduct(productId);

        UserEntity user = commentService.getUser(userId);

        List<CommentEntity> commentEntityList = commentService.getComments(productId,pageable);
        //엔티티 dto로 변환return CommentReadResponseDto.of().str
        return commentEntityList.stream()
                .map(commentEntity -> CommentReadResponseDto.of(commentEntity,commentEntity.getUser()))
                .collect(Collectors.toList());
    }

}
