package org.j1p5.api.block.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.block.validator.BlockValidator;
import org.j1p5.common.dto.Cursor;
import org.j1p5.common.dto.CursorResult;
import org.j1p5.domain.block.BlockUserDto;
import org.j1p5.domain.block.BlockUserInfo;
import org.j1p5.domain.block.entity.BlockEntity;
import org.j1p5.domain.block.repository.BlockRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
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
     * @param cursor
     * @return 차단 유저 정보 리스트
     */
    public CursorResult<BlockUserInfo> read(Long userId, Cursor cursor) {
        UserEntity user = blockValidator.validateUser(userId);
        List<BlockEntity> blockedUsers = blockRepository.findByUserWithCursor(user, cursor.cursor(), cursor.size());
        List<Long> blockedUserIds = blockedUsers.stream()
                .map(b -> b.getBlockedUser().getId()).toList();

        List<BlockUserDto> blockUsers = userRepository.findBlockUserByIds(blockedUserIds);

        List<BlockUserInfo> blockUserInfos = blockUsers.stream()
                .map(dto -> new BlockUserInfo(dto.userId(), dto.nickname(), dto.imageUrl(), dto.emdName()))
                .toList();

        Long nextCursor =
                blockUsers.isEmpty()
                        ? null
                        : blockUsers.get(blockUsers.size() - 1).id(); // 조회된 마지막 물품의 id값 저장

        return CursorResult.of(blockUserInfos, nextCursor);
    }
}
