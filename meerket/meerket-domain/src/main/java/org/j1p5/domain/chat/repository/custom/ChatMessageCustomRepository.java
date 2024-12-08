package org.j1p5.domain.chat.repository.custom;


import org.bson.types.ObjectId;
import org.j1p5.domain.chat.entity.ChatMessageEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageCustomRepository {


    List<ChatMessageEntity> getChatMessageEntities(ObjectId roomObjectId, LocalDateTime beforeTime);
}
