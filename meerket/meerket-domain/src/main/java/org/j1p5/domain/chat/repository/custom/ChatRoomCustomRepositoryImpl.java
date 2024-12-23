package org.j1p5.domain.chat.repository.custom;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.j1p5.domain.chat.command.UpdateChatRoomCommand;
import org.j1p5.domain.chat.entity.ChatRoomEntity;
import org.j1p5.domain.chat.exception.ChatDomainException;
import org.j1p5.domain.global.exception.DomainException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void resetUnreadCount(ObjectId roomObjectId, Long userId) {
        try {
            Query query = new Query(Criteria.where("_id").is(roomObjectId));
            Update update = new Update().set("unreadCounts." + userId, 0);
            UpdateResult result = mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);

            if (result.getMatchedCount() == 0) {
                throw new DomainException(ChatDomainException.NOT_FOUND_CHATROOM);
            }
        } catch (DataAccessException e) {
            throw new DomainException(ChatDomainException.CHAT_ROOM_UPDATE_FAIL);
        }
    }

    @Override
    public void exitChatRoom(Long userId, ObjectId roomId) {
        try {
            Query query = new Query(Criteria.where("_id").is(roomId));
            Update update =
                    new Update().set("userStatus." + userId, false).set("isChatAvailable", false);

            mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);

        } catch (Exception e) {
            throw new DomainException(ChatDomainException.CHAT_EXIT_ERROR);
        }
    }

    @Override
    public void updateChatRoomInfo(UpdateChatRoomCommand command) {

        Query query = new Query(Criteria.where("_id").is(command.roomId()));
        Update update =
                new Update()
                        .set("lastMessage", command.content())
                        .set("lastMessageAt", command.createdAt());

        if (!command.receiverInChatRoom()) {
            update.inc("unreadCounts." + command.receiverId(), 1);
        }

        try {
            mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);
        } catch (Exception e) {
            log.error("채팅방 업데이트 실패");
            throw new DomainException(ChatDomainException.CHAT_ROOM_UPDATE_FAIL);
        }
    }
}
