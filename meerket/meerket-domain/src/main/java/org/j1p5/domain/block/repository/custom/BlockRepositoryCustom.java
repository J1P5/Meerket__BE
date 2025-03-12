package org.j1p5.domain.block.repository.custom;

import org.j1p5.domain.block.entity.BlockEntity;
import org.j1p5.domain.user.entity.UserEntity;

import java.util.List;

public interface BlockRepositoryCustom {
    List<BlockEntity> findByUserWithCursor(UserEntity user, Long cursor, Integer size);
}
