package org.j1p5.domain.comment.repository.querydsl;

import org.j1p5.domain.comment.entity.CommentEntity;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentEntity> findParentCommentByProductId(List<Long> blockUserIds, Long productId, Pageable pageable);
}
