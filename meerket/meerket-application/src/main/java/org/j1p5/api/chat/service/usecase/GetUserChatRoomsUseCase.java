package org.j1p5.api.chat.service.usecase;

import static org.j1p5.api.chat.exception.ChatException.CHATROOM_LIST_ERROR;
import static org.j1p5.api.chat.exception.ChatException.INVALID_ROOM_TYPE;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.api.chat.dto.ChatRoomType;
import org.j1p5.api.chat.dto.OtherProfile;
import org.j1p5.api.chat.dto.response.ChatRoomInfoResponse;
import org.j1p5.api.chat.service.ChatRoomService;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.chat.entity.ChatRoomEntity;
import org.j1p5.domain.chat.repository.ChatRoomRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * @author yechan 유저가 속한 채팅방 목록 조회
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetUserChatRoomsUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomInfoResponse> execute(Long userId, ChatRoomType type) {
        return getUserChatRooms(userId, type);
    }

    private List<ChatRoomInfoResponse> getUserChatRooms(Long userId, ChatRoomType type) {
        List<ChatRoomEntity> chatRoomEntities;
        try {
            chatRoomEntities = getChatRoomEntities(userId, type);

            List<ChatRoomInfoResponse> chatRoomInfoResponses = new ArrayList<>();
            for (ChatRoomEntity chatRoomEntity : chatRoomEntities) {
                OtherProfile otherProfile = chatRoomService.getOtherProfile(chatRoomEntity, userId);

                ChatRoomInfoResponse response =
                        new ChatRoomInfoResponse(
                                userId,
                                chatRoomEntity.getId().toString(),
                                chatRoomEntity.getLastMessage(),
                                chatRoomEntity.getLastMessageAt(),
                                chatRoomEntity.getProductId(),
                                chatRoomEntity.getUnreadCounts().getOrDefault(userId, 0),
                                chatRoomEntity.getProductImage(),
                                otherProfile.otherNickname(),
                                otherProfile.otherProfileImage());

                chatRoomInfoResponses.add(response);
            }
            return chatRoomInfoResponses;

        } catch (DataAccessException e) {
            log.error("채팅방 목록 조회중 에러 발생", e);
            throw new WebException(CHATROOM_LIST_ERROR);
        }
    }

    private List<ChatRoomEntity> getChatRoomEntities(Long userId, ChatRoomType type) {
        return switch (type) {
            case ALL -> chatRoomRepository.findByUserId(userId);
            case PURCHASE -> chatRoomRepository.findByBuyerId(userId);
            case SALE -> chatRoomRepository.findBySellerId(userId);
            default -> throw new WebException(INVALID_ROOM_TYPE);
        };
    }
}
