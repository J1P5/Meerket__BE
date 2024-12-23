package org.j1p5.domain.comment.repository;

import java.util.List;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.comment.repository.querydsl.CommentRepositoryCustom;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository
        extends JpaRepository<CommentEntity, Long>, CommentRepositoryCustom {
    List<CommentEntity> findByUser(UserEntity user);
}
