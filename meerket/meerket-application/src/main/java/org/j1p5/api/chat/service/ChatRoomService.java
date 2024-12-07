package org.j1p5.api.chat.service;

import com.mongodb.client.result.UpdateResult;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.ChatRoomBasicInfo;
import org.j1p5.api.chat.dto.ChatRoomType;
import org.j1p5.api.chat.dto.OtherProfile;
import org.j1p5.api.chat.dto.response.ChatRoomInfoResponse;
import org.j1p5.api.chat.dto.response.CreateChatRoomResponse;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.chat.entity.ChatRoomEntity;
import org.j1p5.domain.chat.repository.ChatRoomRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.redis.RedisService;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.j1p5.api.chat.exception.ChatException.*;
import static org.j1p5.api.product.exception.ProductException.PRODUCT_NOT_FOUND;

/**
 * @author yechan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final RedisService redisService;

    /**
     * 특정 채팅방에 유저가 속해있는지 확인
     *
     * @param userId
     * @param roomId
     */
    public void verifyAccess(Long userId, ObjectId roomId){
        ChatRoomEntity chatRoomEntity =
                chatRoomRepository
                        .findById(roomId)
                        .orElseThrow(() -> new WebException(NOT_FOUND_CHATROOM));

        if (chatRoomEntity.getSellerId() != userId && chatRoomEntity.getBuyerId() != userId) {
            throw new WebException(NOT_MEMBER_OF_CHATROOM);
        }
    }

    /**
     * String 타입의 roomId 검증 후 ObjectId로 반환
     *
     * @param roomId
     * @return
     */
    public ObjectId validateRoomId(String roomId) {
        if (!ObjectId.isValid(roomId)) {
            throw new WebException(INVALID_ROOM_ID);
        }
        return new ObjectId(roomId);
    }

    public void updateChatRoomInfo(
            ObjectId roomId,
            String content,
            Long receiverId,
            boolean receiverInChatRoom,
            LocalDateTime createdAt) {

        Query query = new Query(Criteria.where("_id").is(roomId));
        Update update = new Update().set("lastMessage", content).set("lastMessageAt", createdAt);

        if (!receiverInChatRoom) {
            update.inc("unreadCounts." + receiverId, 1);
        }

        try {
            mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);
        } catch (Exception e) {
            log.error("채팅방 업데이트 실패");
            throw new WebException(CHAT_ROOM_UPDATE_FAIL);
        }
    }

    /**
     * 채팅방에 상대방이 있는지 확인
     *
     * @param roomId 채팅방 id
     * @param receiverId 상대방 id
     * @return 상대방이 있으면 true, 없으면 false
     */
    public boolean isReceiverInChatRoom(ObjectId roomId, String receiverId) {

        try {
            String userCurrentRoom = redisService.getUserCurrentRoom(receiverId);

            return roomId.toString().equals(userCurrentRoom);
        } catch (Exception e) {
            throw new WebException(CHAT_RECEIVER_FIND_ERROR);
        }
    }

    public List<ChatRoomInfoResponse> getUserChatRooms(Long userId, ChatRoomType type) {
        List<ChatRoomEntity> chatRoomEntities;
        try {
            chatRoomEntities =
                    switch (type) {
                        case ALL -> chatRoomRepository.findByUserId(userId);
                        case PURCHASE -> chatRoomRepository.findByBuyerId(userId);
                        case SALE -> chatRoomRepository.findBySellerId(userId);
                        default ->
                                throw new WebException(INVALID_ROOM_TYPE);
                    };

            List<ChatRoomInfoResponse> chatRoomInfoResponses = new ArrayList<>();
            for (ChatRoomEntity chatRoomEntity : chatRoomEntities) {
                OtherProfile otherProfile = getOtherProfile(chatRoomEntity, userId);

                ChatRoomInfoResponse response =
                        new ChatRoomInfoResponse(
                                userId,
                                chatRoomEntity.getId().toString(),
                                chatRoomEntity.getLastMessage(),
                                chatRoomEntity.getLastMessageAt(),
                                chatRoomEntity.getProductId(),
                                chatRoomEntity.getUnreadCounts().getOrDefault(userId, 0),
                                chatRoomEntity.getProductImage(),
                                otherProfile.otherNickname(),
                                otherProfile.otherProfileImage());

                chatRoomInfoResponses.add(response);
            }
            return chatRoomInfoResponses;

        } catch (DataAccessException e) {
            log.error("채팅방 목록 조회중 에러 발생", e);
            throw new WebException(CHATROOM_LIST_ERROR);
        }
    }

    public CreateChatRoomResponse createChatRoom(AuctionEntity auctionEntity, Long productId) {

        ProductEntity productEntity =
                productRepository
                        .findById(productId)
                        .orElseThrow(() -> new WebException(PRODUCT_NOT_FOUND));

        ChatRoomEntity chatRoomEntity =
                ChatRoomEntity.create(
                        productEntity.getUser().getId(),
                        auctionEntity.getUser().getId(),
                        productId,
                        productEntity.getThumbnail(),
                        productEntity.getTitle(),
                        auctionEntity.getPrice());

        chatRoomRepository.save(chatRoomEntity);

        return new CreateChatRoomResponse(chatRoomEntity.getId().toString());
    }

    public void exitChatRoom(Long userId, ObjectId roomId) {
        try {
            Query query = new Query(Criteria.where("_id").is(roomId));
            Update update =
                    new Update().set("userStatus." + userId, false).set("isChatAvailable", false);

            mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);

        } catch (Exception e) {
            throw new WebException(CHAT_EXIT_ERROR);
        }
    }

    public void resetUnreadCount(ObjectId roomObjectId, Long userId) {
        try {
            Query query = new Query(Criteria.where("_id").is(roomObjectId));
            Update update = new Update().set("unreadCounts." + userId, 0);
            UpdateResult result = mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);

            if (result.getMatchedCount() == 0) {
                throw new WebException(NOT_FOUND_CHATROOM);
            }
        } catch (DataAccessException e) {
            throw new WebException(CHAT_ROOM_UPDATE_FAIL);
        }
    }

    public ChatRoomBasicInfo getChatRoomBasicInfo(ObjectId roomObjectId, Long userId) {
        ChatRoomEntity chatRoomEntity =
                chatRoomRepository
                        .findById(roomObjectId)
                        .orElseThrow(() -> new WebException(NOT_FOUND_CHATROOM));

        OtherProfile otherProfile = getOtherProfile(chatRoomEntity, userId);
        boolean isSeller = chatRoomEntity.getSellerId() == userId;

        return new ChatRoomBasicInfo(
                chatRoomEntity.getId().toString(),
                otherProfile.otherNickname(),
                otherProfile.otherProfileImage(),
                otherProfile.otherUserId(),
                chatRoomEntity.getProductId(),
                chatRoomEntity.getProductTitle(),
                chatRoomEntity.getProductImage(),
                chatRoomEntity.getPrice(),
                isSeller,
                chatRoomEntity.isChatAvailable());
    }


    // 상대방 프로필 확인
    private OtherProfile getOtherProfile(ChatRoomEntity chatRoomEntity, Long userId) {
        Long otherUserId =
                (userId == chatRoomEntity.getSellerId())
                        ? chatRoomEntity.getBuyerId()
                        : chatRoomEntity.getSellerId();

        UserEntity userEntity =
                userRepository
                        .findById(otherUserId)
                        .orElseThrow(() -> new WebException(CHAT_RECEIVER_NOT_FOUND));

        return new OtherProfile(
                userEntity.getNickname(), userEntity.getImageUrl(), userEntity.getId());
    }
}
