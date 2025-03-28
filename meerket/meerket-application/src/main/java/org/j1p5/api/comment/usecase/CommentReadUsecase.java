package org.j1p5.api.comment.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.comment.dto.response.CommentReadResponseDto;
import org.j1p5.api.comment.service.CommentService;
import org.j1p5.domain.comment.CommentInfo;
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


    /**
     * 댓글 조회
     * @author sunghyun
     * @param productId
     * @param userId
     * @param pageable
     * @return
     */
    public List<CommentReadResponseDto> getAllComments(Long productId, Long userId, Pageable pageable){


        ProductEntity product = commentService.getProduct(productId);

        UserEntity user = commentService.getUser(userId);

        List<CommentInfo> commentInfos = commentService.getComments(user, productId,pageable); //일단 부모 댓글만 가져오기
        //엔티티 dto로 변환return CommentReadResponseDto.of().str
        return commentInfos.stream()
                .map(CommentReadResponseDto::of)
                .collect(Collectors.toList());
    }

}
