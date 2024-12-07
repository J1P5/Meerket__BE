package org.j1p5.domain.chat.repository.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements  ChatRoomCustomRepository{

    private final MongoTemplate mongoTemplate;

}
