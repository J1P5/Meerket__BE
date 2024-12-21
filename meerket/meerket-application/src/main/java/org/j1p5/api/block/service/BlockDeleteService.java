package org.j1p5.api.block.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.block.validator.BlockValidator;
import org.j1p5.domain.block.entity.BlockEntity;
import org.j1p5.domain.block.repository.BlockRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockDeleteService {

    private final BlockRepository blockRepository;
    private final BlockValidator blockValidator;

    /**
     * 차단 해제
     * @author icecoff22
     * @param userId
     * @param unblockId
     */
    @Transactional
    public void delete(Long userId, Long unblockId) {
        UserEntity user = blockValidator.validateUser(userId);
        UserEntity blockedUser = blockValidator.validateUser(unblockId);

        blockRepository.deleteByUserAndBlockedUser(user, blockedUser);
    }

    /**
     * 회원 탈퇴 시 차단 테이블 delete 업데이트
     * @author icecoff22
     * @param user
     */
    @Transactional
    public void withdraw(UserEntity user) {
        List<BlockEntity> blocks = blockRepository.findByUser(user);

        if (blocks.isEmpty()) {
            return;
        }

        blocks.forEach(BlockEntity::withdraw);
    }
}
