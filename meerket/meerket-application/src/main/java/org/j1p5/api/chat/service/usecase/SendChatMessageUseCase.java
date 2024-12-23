package org.j1p5.api.chat.service.usecase;

import static org.j1p5.api.chat.exception.ChatException.CHAT_RECEIVER_FIND_ERROR;
import static org.j1p5.api.chat.exception.ChatException.CHAT_SAVE_ERROR;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.chat.dto.response.ChatSocketMessageResponse;
import org.j1p5.api.chat.service.ChatRoomService;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.chat.command.UpdateChatRoomCommand;
import org.j1p5.domain.chat.entity.ChatMessageEntity;
import org.j1p5.domain.chat.repository.ChatMessageRepository;
import org.j1p5.domain.chat.repository.ChatRoomRepository;
import org.j1p5.domain.chat.vo.MessageInfo;
import org.j1p5.domain.redis.RedisService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yechan 메시지를 보냈을때 메시지 저장과 채팅방 상태 업데이트
 */
@Service
@RequiredArgsConstructor
public class SendChatMessageUseCase {

    private final ChatRoomService chatRoomService;
    private final FcmService fcmService;
    private final RedisService redisService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 메시지 보내기
    // @Transactional 추후 적용
    public ChatMessageResponse execute(MessageInfo messageInfo) {

        Long userId = messageInfo.getUserId();
        Long receiverId = messageInfo.getReceiverId();
        String roomId = messageInfo.getRoomId();
        String content = messageInfo.getContent();

        ObjectId roomObjectId = chatRoomService.validateRoomId(roomId);

        chatRoomService.verifyAccess(userId, roomObjectId);

        ChatMessageEntity chatMessageEntity = saveMessage(roomObjectId, userId, content);

        boolean receiverInChatRoom = isReceiverInChatRoom(roomObjectId, receiverId.toString());

        chatRoomRepository.updateChatRoomInfo(
                new UpdateChatRoomCommand(
                        roomObjectId,
                        content,
                        receiverId,
                        receiverInChatRoom,
                        chatMessageEntity.getCreatedAt()));

        sendWebSocketMessage(chatMessageEntity);

        if (!receiverInChatRoom)
            fcmService.sendFcmChatMessage(
                    roomId, receiverId, userId, chatMessageEntity.getContent());

        return ChatMessageResponse.fromEntity(chatMessageEntity);
    }

    private ChatMessageEntity saveMessage(ObjectId roomId, Long senderId, String content) {

        ChatMessageEntity chatMessageEntity = ChatMessageEntity.create(roomId, senderId, content);

        try {
            return chatMessageRepository.save(chatMessageEntity);
        } catch (Exception e) {
            throw new WebException(CHAT_SAVE_ERROR);
        }
    }

    private boolean isReceiverInChatRoom(ObjectId roomId, String receiverId) {

        try {
            String userCurrentRoom = redisService.getUserCurrentRoom(receiverId);

            return roomId.toString().equals(userCurrentRoom);
        } catch (Exception e) {
            throw new WebException(CHAT_RECEIVER_FIND_ERROR);
        }
    }

    private void sendWebSocketMessage(ChatMessageEntity chatMessageEntity) {
        ChatSocketMessageResponse response =
                ChatSocketMessageResponse.fromEntity(chatMessageEntity);

        simpMessagingTemplate.convertAndSend(
                "/sub/chatroom/" + chatMessageEntity.getRoomId().toString(), response);
    }
}
