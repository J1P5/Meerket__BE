package org.j1p5.api.comment.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.comment.service.CommentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDeleteUsecase {
    private final CommentService commentService;

    /**
     * 댓글 삭제
     *
     * @author sunghyun
     * @param productId
     * @param userId
     * @param commentId
     */
    @Transactional
    public void removeComment(Long productId, Long userId, Long commentId) {
        commentService.removedComment(productId, userId, commentId);
    }
}
