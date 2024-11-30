package org.j1p5.api.chat.service.usecase;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.service.ChatRoomService;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

/**
 * @author yechan
 * 채팅방에서 나갔을때 채팅방의 활성 유저 상태 변경
 */
@Service
@RequiredArgsConstructor
public class ExitChatRoomUseCase {

    private final ChatRoomService chatRoomService;

    public void execute(Long userId, String roomId) throws AccessDeniedException {
        ObjectId roomObjectId = chatRoomService.validateRoomId(roomId);

        chatRoomService.verifyAccess(userId, roomObjectId);

        chatRoomService.exitChatRoom(userId, roomObjectId);
    }
}
