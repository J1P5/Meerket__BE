package org.j1p5.domain.block.repository.querydsl;

import org.j1p5.domain.block.BlockUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlockRepositoryCustom {
    Page<BlockUserInfo> getBlockUsers(Long userId, Pageable pageable);



}
