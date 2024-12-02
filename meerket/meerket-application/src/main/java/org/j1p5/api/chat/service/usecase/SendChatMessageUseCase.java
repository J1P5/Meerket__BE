package org.j1p5.api.chat.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.chat.service.ChatMessageService;
import org.j1p5.api.chat.service.ChatRoomService;
import org.j1p5.domain.chat.entity.ChatMessageEntity;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;

import java.nio.file.AccessDeniedException;

/**
 * 메시지를 보냈을때 메시지 저장과 채팅방 상태 업데이트
 */
@Service
@RequiredArgsConstructor
public class SendChatMessageUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // 메시지 보내기
    //@Transactional 추후 적용
    public ChatMessageResponse execute(
            Long userId, Long receiverId, String content, String roomId) throws AccessDeniedException {

        ObjectId roomObjectId = chatRoomService.validateRoomId(roomId);

        chatRoomService.verifyAccess(userId, roomObjectId);

        ChatMessageEntity chatMessageEntity = chatMessageService.saveMessage(roomObjectId, userId, content);

        boolean receiverInChatRoom = chatRoomService.isReceiverInChatRoom(roomObjectId, receiverId);

        chatRoomService.updateChatRoomInfo(
                roomObjectId, content,
                receiverId, receiverInChatRoom, chatMessageEntity.getCreatedAt());

        chatMessageService.sendWebSocketMessage(roomId, userId, content);

        chatRoomService.sendFcmMessage(receiverInChatRoom, receiverId, userId, chatMessageEntity.getContent());

        return ChatMessageResponse.fromEntity(chatMessageEntity);
    }


}
