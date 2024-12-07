package org.j1p5.domain.chat.repository.custom;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.domain.chat.entity.ChatMessageEntity;
import org.j1p5.domain.chat.exception.ChatDomainException;
import org.j1p5.domain.global.exception.DomainException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageCustomRepositoryImpl implements  ChatMessageCustomRepository{

    private final MongoTemplate mongoTemplate;

    private static final int CHAT_LIST_LIMIT = 30;


    @Override
    public List<ChatMessageEntity> getChatMessageEntities(ObjectId roomObjectId, LocalDateTime beforeTime) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("roomId").is(roomObjectId));

            if (beforeTime != null) {
                query.addCriteria(Criteria.where("createdAt").lt(beforeTime));
            }

            query.with(Sort.by(Sort.Direction.DESC, "createdAt")).limit(CHAT_LIST_LIMIT);

            List<ChatMessageEntity> chatMessageEntities = mongoTemplate.find(query, ChatMessageEntity.class);

            return chatMessageEntities;
        } catch (Exception e) {
            throw new DomainException(ChatDomainException.CHAT_READ_ERROR);
        }
    }
}
