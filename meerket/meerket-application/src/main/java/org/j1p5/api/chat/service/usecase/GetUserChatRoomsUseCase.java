package org.j1p5.api.chat.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.chat.dto.ChatRoomFilter;
import org.j1p5.api.chat.dto.response.ChatRoomInfoResponse;
import org.j1p5.api.chat.service.ChatRoomService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yechan
 * 유저가 속한 채팅방 목록 조회
 */
@Service
@RequiredArgsConstructor
public class GetUserChatRoomsUseCase {

    private final ChatRoomService chatRoomService;

    public List<ChatRoomInfoResponse> execute(Long userId, ChatRoomFilter filter) {
        return chatRoomService.getUserChatRooms(userId, filter);
    }
}
