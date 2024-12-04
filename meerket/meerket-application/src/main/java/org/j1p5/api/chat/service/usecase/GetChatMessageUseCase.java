package org.j1p5.api.chat.service.usecase;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.chat.service.ChatMessageService;
import org.j1p5.api.chat.service.ChatRoomService;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yechan
 * 채팅 메시지를 불러옴 넘겨받은 가장 오래된 메시지 시간 기준 30개
 */
@Service
@RequiredArgsConstructor
public class GetChatMessageUseCase {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    public List<ChatMessageResponse> execute(String roomId, LocalDateTime beforeTime, Long userId){

        ObjectId roomObjectId = chatRoomService.validateRoomId(roomId);

        chatRoomService.verifyAccess(userId, roomObjectId);

        return chatMessageService.getChatMessages(roomObjectId, beforeTime);
    }

}
