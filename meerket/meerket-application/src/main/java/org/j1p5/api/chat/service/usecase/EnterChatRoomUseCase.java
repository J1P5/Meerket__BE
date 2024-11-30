package org.j1p5.api.chat.service.usecase;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.ChatRoomBasicInfo;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.chat.dto.response.ChatRoomEnterResponse;
import org.j1p5.api.chat.service.ChatMessageService;
import org.j1p5.api.chat.service.ChatRoomService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yechan
 * 특정 채팅방에 입장했을때 여러 채팅 정보들 제공
 */
@Service
@RequiredArgsConstructor
public class EnterChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;


    public ChatRoomEnterResponse execute(Long userId, String roomId) {

        ObjectId roomObjectId = chatRoomService.validateRoomId(roomId);

        chatRoomService.resetUnreadCount(roomObjectId, userId);

        List<ChatMessageResponse> chatMessages = chatMessageService.getChatMessages(roomObjectId, userId, null);

        ChatRoomBasicInfo chatRoomBasicInfo = chatRoomService.getChatRoomBasicInfo(roomObjectId, userId);

        return new ChatRoomEnterResponse(chatRoomBasicInfo, chatMessages);
    }

}
