package org.j1p5.domain.comment.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.comment.entity.QCommentEntity;
import org.springframework.data.domain.Pageable;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CommentRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<CommentEntity> findParentCommentByProductId(Long productId, Pageable pageable) {
        QCommentEntity comment = QCommentEntity.commentEntity;

        return jpaQueryFactory
                .selectFrom(comment)
                // .leftJoin(comment.replies).fetchJoin()
                .where(
                        comment.product
                                .id
                                .eq(productId) // q객체명, 자바 변수명(!= 컬럼명)
                                .and(comment.parentComment.isNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
