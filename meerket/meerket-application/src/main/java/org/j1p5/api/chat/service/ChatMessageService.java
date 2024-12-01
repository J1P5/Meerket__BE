package org.j1p5.api.chat.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.domain.chat.entity.ChatMessageEntity;
import org.j1p5.domain.chat.repository.ChatMessageRepository;
import org.springframework.boot.autoconfigure.mongo.StandardMongoClientSettingsBuilderCustomizer;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yechan
 */
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;

    public ChatMessageEntity sendMessage(ObjectId roomId, Long senderId, String content) {
        ChatMessageEntity chatMessageEntity = ChatMessageEntity.create(roomId, senderId, content);
        try {
            return chatMessageRepository.save(chatMessageEntity);
        } catch (Exception e) { //TODO 추후 커스텀 예외 처리
            throw new RuntimeException("메시지 저장에 실패했습니다.", e);
        }
    }


    public List<ChatMessageResponse> getChatMessages(ObjectId roomObjectId, Long userId, LocalDateTime beforeTime) {

        int limit = 30;
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomObjectId));

        if (beforeTime != null) {
            query.addCriteria(Criteria.where("createdAt").lt(beforeTime));
        }

        query.with(Sort.by(Sort.Direction.DESC, "createdAt")).limit(limit);

        List<ChatMessageEntity> chatMessageEntities = mongoTemplate.find(query, ChatMessageEntity.class);
        List<ChatMessageResponse> responses = chatMessageEntities.stream()
                .map(ChatMessageResponse::fromEntity)
                .toList();

        return responses;
    }
}
