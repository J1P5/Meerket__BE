package org.j1p5.domain.user.repository.custom;

import org.j1p5.domain.block.BlockUserDto;

import java.util.List;

public interface UserRepositoryCustom {
    List<BlockUserDto> findBlockUserByIds(List<Long> userIds);
}
