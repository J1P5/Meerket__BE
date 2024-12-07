package org.j1p5.api.chat.service.usecase;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.ChatRoomBasicInfo;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.chat.dto.response.ChatRoomEnterResponse;
import org.j1p5.api.chat.exception.ChatException;
import org.j1p5.api.chat.service.ChatMessageService;
import org.j1p5.api.chat.service.ChatRoomService;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.chat.entity.ChatRoomEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private final MongoTemplate mongoTemplate;


    public ChatRoomEnterResponse execute(Long userId, String roomId) {

        ObjectId roomObjectId = chatRoomService.validateRoomId(roomId);

        resetUnreadCount(roomObjectId, userId);

        List<ChatMessageResponse> chatMessages = chatMessageService.getChatMessages(
                roomObjectId, null);

        ChatRoomBasicInfo chatRoomBasicInfo = chatRoomService.getChatRoomBasicInfo(roomObjectId, userId);

        return new ChatRoomEnterResponse(chatRoomBasicInfo, chatMessages);
    }


    private void resetUnreadCount(ObjectId roomObjectId, Long userId) {
        try {
            Query query = new Query(Criteria.where("_id").is(roomObjectId));
            Update update = new Update().set("unreadCounts." + userId, 0);
            UpdateResult result = mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);


            if (result.getMatchedCount() == 0) {
                throw new WebException(ChatException.NOT_FOUND_CHATROOM);
            }
        } catch (DataAccessException e) {
            throw new WebException(ChatException.CHAT_ROOM_UPDATE_FAIL);
        }
    }

}
