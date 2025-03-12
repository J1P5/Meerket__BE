package org.j1p5.domain.block.repository;

import org.j1p5.domain.block.entity.BlockEntity;
import org.j1p5.domain.block.repository.custom.BlockRepositoryCustom;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockRepository extends JpaRepository<BlockEntity, Long>, BlockRepositoryCustom {
    List<BlockEntity> findByUser(UserEntity user);
    void deleteByUserAndBlockedUser(UserEntity user, UserEntity blockedUser);
}
