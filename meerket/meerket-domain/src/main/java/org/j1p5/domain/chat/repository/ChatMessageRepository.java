package org.j1p5.domain.chat.repository;

import org.bson.types.ObjectId;
import org.j1p5.domain.chat.entity.ChatMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChatMessageRepository extends MongoRepository<ChatMessageEntity, ObjectId> {
}
