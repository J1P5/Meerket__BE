package org.j1p5.domain.chat.repository;

import org.bson.types.ObjectId;
import org.j1p5.domain.chat.entity.ChatMessageEntity;
import org.j1p5.domain.chat.repository.custom.ChatMessageCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository
        extends MongoRepository<ChatMessageEntity, ObjectId>, ChatMessageCustomRepository {}
