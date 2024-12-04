package org.j1p5.api.chat.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.chat.dto.response.ChatSocketMessageResponse;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.chat.entity.ChatMessageEntity;
import org.j1p5.domain.chat.repository.ChatMessageRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.j1p5.api.chat.exception.ChatException.*;

/**
 * @author yechan
 */
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private static final int CHAT_LIST_LIMIT = 30;
    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatMessageEntity saveMessage(ObjectId roomId, Long senderId, String content) {
        ChatMessageEntity chatMessageEntity = ChatMessageEntity.create(roomId, senderId, content);
        try {
            return chatMessageRepository.save(chatMessageEntity);
        } catch (Exception e) {
            throw new WebException(CHAT_SAVE_ERROR);
        }
    }


    public List<ChatMessageResponse> getChatMessages(ObjectId roomObjectId, LocalDateTime beforeTime) {

        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("roomId").is(roomObjectId));

            if (beforeTime != null) {
                query.addCriteria(Criteria.where("createdAt").lt(beforeTime));
            }

            query.with(Sort.by(Sort.Direction.DESC, "createdAt")).limit(CHAT_LIST_LIMIT);

            List<ChatMessageEntity> chatMessageEntities = mongoTemplate.find(query, ChatMessageEntity.class);

            List<ChatMessageResponse> responses = chatMessageEntities.stream()
                    .map(ChatMessageResponse::fromEntity)
                    .collect(Collectors.toList());

            Collections.reverse(responses);

            return responses;
        } catch (Exception e) {
            throw new WebException(CHAT_READ_ERROR);
        }
    }


    public void sendWebSocketMessage(ChatMessageEntity chatMessageEntity) {
        ChatSocketMessageResponse response = ChatSocketMessageResponse.fromEntity(chatMessageEntity);

        simpMessagingTemplate.convertAndSend("/sub/chatroom/" +
                chatMessageEntity.getRoomId().toString(), response);

    }
}
