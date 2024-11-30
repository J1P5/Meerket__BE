package org.j1p5.api.chat.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.chat.dto.response.CreateChatRoomResponse;
import org.j1p5.api.chat.service.ChatRoomService;
import org.springframework.stereotype.Service;

/**
 * @author yechan
 * 낙찰자와 판매자 1대1 채팅방 생성
 */
@Service
@RequiredArgsConstructor
public class CreateChatRoomUseCase {

    private final ChatRoomService chatRoomService;

    public CreateChatRoomResponse execute(Long userId, Long productId) {
        return chatRoomService.createChatRoom(userId, productId);
    }
}
