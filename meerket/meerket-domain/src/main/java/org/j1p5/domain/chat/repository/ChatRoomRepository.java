package org.j1p5.domain.chat.repository;

import org.bson.types.ObjectId;
import org.j1p5.domain.chat.entity.ChatRoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoomEntity, ObjectId> {
    // 모든 채팅방 조회 (userId가 sellerId 또는 buyerId인 경우)
    @Query(value = "{ '$or': [ { 'sellerId': ?0, 'userStatus.?0': true }, { 'buyerId': ?0, 'userStatus.?0': true } ] }", sort = "{ 'lastMessageAt': -1 }")
    List<ChatRoomEntity> findByUserId(Long userId);

    // sellerId인 경우만 조회
    @Query(value = "{ 'sellerId': ?0, 'userStatus.?0': true }", sort = "{ 'lastMessageAt': -1 }")
    List<ChatRoomEntity> findBySellerId(Long sellerId);

    // buyerId인 경우만 조회
    @Query(value = "{ 'buyerId': ?0, 'userStatus.?0' : true }", sort = "{ 'lastMessageAt': -1 }")
    List<ChatRoomEntity> findByBuyerId(Long buyerId);
}
