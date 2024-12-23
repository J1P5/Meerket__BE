package org.j1p5.domain.block.repository;

import java.util.List;
import org.j1p5.domain.block.entity.BlockEntity;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<BlockEntity, Long> {
    List<BlockEntity> findByUser(UserEntity user);

    void deleteByUserAndBlockedUser(UserEntity user, UserEntity blockedUser);
}
