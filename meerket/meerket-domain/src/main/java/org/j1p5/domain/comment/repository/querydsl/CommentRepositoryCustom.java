package org.j1p5.domain.comment.repository.querydsl;

import java.util.List;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    List<CommentEntity> findParentCommentByProductId(Long productId, Pageable pageable);
}
