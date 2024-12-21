package org.j1p5.api.block.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.block.validator.BlockValidator;
import org.j1p5.common.dto.PageResult;
import org.j1p5.domain.block.BlockUserInfo;
import org.j1p5.domain.block.entity.BlockEntity;
import org.j1p5.domain.block.repository.BlockRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockReadService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final BlockValidator blockValidator;

    /**
     * 차단 조회
     * @author icecoff22
     * @param userId
     * @param page
     * @param size
     * @return 차단 유저 정보 리스트
     */
    public PageResult<BlockUserInfo> read(Long userId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        UserEntity user = blockValidator.validateUser(userId);
        List<BlockEntity> blockedUsers = blockRepository.findByUser(user);
        List<Long> blockedUserIds = blockedUsers.stream()
                .map(b -> b.getBlockedUser().getId()).toList();

        Page<BlockUserInfo> blockUserInfos = userRepository.findBlockUserByIds(blockedUserIds, pageRequest);
        return PageResult.of(blockUserInfos.getContent(), blockUserInfos.getTotalPages(), blockUserInfos.hasNext());
    }
}
