package org.j1p5.api.chat.service.usecase;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.service.ChatRoomService;
import org.j1p5.domain.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;


/**
 * @author yechan
 * 채팅방에서 나갔을때 채팅방의 활성 유저 상태 변경
 */
@Service
@RequiredArgsConstructor
public class ExitChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    public void execute(Long userId, String roomId) {
        ObjectId roomObjectId = chatRoomService.validateRoomId(roomId);

        chatRoomService.verifyAccess(userId, roomObjectId);

        chatRoomRepository.exitChatRoom(userId, roomObjectId);
    }
}
