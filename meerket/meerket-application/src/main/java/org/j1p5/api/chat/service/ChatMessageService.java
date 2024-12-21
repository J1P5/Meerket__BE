package org.j1p5.api.chat.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.chat.entity.ChatMessageEntity;
import org.j1p5.domain.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.j1p5.api.chat.exception.ChatException.CHAT_READ_ERROR;

/**
 * @author yechan
 */
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;


    public List<ChatMessageResponse> getChatMessages(ObjectId roomObjectId, LocalDateTime beforeTime) {
        try {

            List<ChatMessageEntity> chatMessageEntities = chatMessageRepository
                    .getChatMessageEntities(roomObjectId, beforeTime);

            List<ChatMessageResponse> responses = chatMessageEntities.stream()
                    .map(ChatMessageResponse::fromEntity)
                    .collect(Collectors.toList());

            Collections.reverse(responses);

            return responses;
        } catch (Exception e) {
            throw new WebException(CHAT_READ_ERROR);
        }
    }

}
