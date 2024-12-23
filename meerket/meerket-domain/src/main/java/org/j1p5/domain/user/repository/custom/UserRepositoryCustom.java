package org.j1p5.domain.user.repository.custom;

import java.util.List;
import org.j1p5.domain.block.BlockUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<BlockUserInfo> findBlockUserByIds(List<Long> userIds, Pageable pageable);
}
