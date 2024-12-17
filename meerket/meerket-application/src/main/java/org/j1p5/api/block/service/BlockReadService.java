package org.j1p5.api.block.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.user.exception.UserNotFoundException;
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

import static org.j1p5.api.global.excpetion.WebErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BlockReadService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    public PageResult<BlockUserInfo> read(Long userId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        UserEntity user = validateUserId(userId);
        List<BlockEntity> blockedUsers = blockRepository.findByUser(user);
        List<Long> blockedUserIds = blockedUsers.stream()
                .map(b -> b.getBlockedUserId().getId()).toList();

        Page<BlockUserInfo> blockUserInfos = userRepository.findBlockUserByIds(blockedUserIds, pageRequest);
        return PageResult.of(blockUserInfos.getContent(), blockUserInfos.getTotalPages(), blockUserInfos.hasNext());
    }

    private UserEntity validateUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }
}
