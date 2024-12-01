package org.j1p5.api.chat.service;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.ChatRoomBasicInfo;
import org.j1p5.api.chat.dto.ChatRoomFilter;
import org.j1p5.api.chat.dto.OtherProfile;
import org.j1p5.api.chat.dto.response.ChatRoomInfoResponse;
import org.j1p5.api.chat.dto.response.CreateChatRoomResponse;
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

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;

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
     * @throws AccessDeniedException
     */
    public void verifyAccess(Long userId, ObjectId roomId) throws AccessDeniedException {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        if (chatRoomEntity.getSellerId() != userId && chatRoomEntity.getBuyerId() != userId) {
            //TODO 예외처리 추가하기
            throw new AccessDeniedException("해당 채팅방에 속한 유저가 아닙니다.");
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
            throw new IllegalArgumentException("roomId 형식이 맞지않습니다.");
        }
        return new ObjectId(roomId);
    }

    public void updateChatRoomInfo(ObjectId roomId, String content, Long receiverId
            , boolean receiverInChatRoom, LocalDateTime createdAt) {

        Query query = new Query(Criteria.where("_id").is(roomId));
        Update update = new Update()
                .set("lastMessage", content)
                .set("lastMessageAt", createdAt);

        if (!receiverInChatRoom) {
            update.inc("unreadCounts." + receiverId, 1);
        }

        try {
            mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);
        } catch (Exception e) { //TODO 커스텀 예외 처리
            log.error("채팅방 업데이트 실패");
            throw new RuntimeException("채팅방 업데이트에 실패");
        }
    }

    /**
     * 채팅방에 상대방이 있는지 확인
     *
     * @param roomId 채팅방 id
     * @param receiverId 상대방 id
     * @return 상대방이 있으면 true, 없으면 false
     */
    public boolean isReceiverInChatRoom(ObjectId roomId, Long receiverId) {

        String userCurrentRoom = redisService.getUserCurrentRoom(receiverId);

        return roomId.toString().equals(userCurrentRoom);
    }


    public List<ChatRoomInfoResponse> getUserChatRooms(Long userId, ChatRoomFilter filter) {
        //TODO 커스텀 예외 처리
        List<ChatRoomEntity> chatRoomEntities;
        try {
            chatRoomEntities = switch (filter) {
                case ALL -> chatRoomRepository.findAllByUserId(userId);
                case PURCHASE -> chatRoomRepository.findByBuyerId(userId);
                case SALE -> chatRoomRepository.findBySellerId(userId);
                default -> throw new IllegalArgumentException("잘못된 필터입력입니다."); //TODO 추후 예외처리
            };

            List<ChatRoomInfoResponse> chatRoomInfoResponses = new ArrayList<>();
            for (ChatRoomEntity chatRoomEntity : chatRoomEntities) {
                OtherProfile otherProfile = getOtherProfile(chatRoomEntity, userId);

                ChatRoomInfoResponse response = new ChatRoomInfoResponse(
                        chatRoomEntity.getId().toString(),
                        chatRoomEntity.getLastMessage(),
                        chatRoomEntity.getLastMessageAt(),
                        chatRoomEntity.getProductId(),
                        chatRoomEntity.getUnreadCounts().getOrDefault(userId, 0),
                        chatRoomEntity.getProductImage(),
                        otherProfile.otherNickname(),
                        otherProfile.otherProfileImage()
                );

                chatRoomInfoResponses.add(response);
            }
            return chatRoomInfoResponses;

        } catch (DataAccessException e) {
            log.error("채팅방 목록 조회중 에러 발생", e);
            throw new RuntimeException("채팅방 조회중 에러 발생");
        }
    }


    public CreateChatRoomResponse createChatRoom(Long userId, Long productId) {
        //TODO 낙찰자, 구매자 찾는과정 필요
        // 지금은 userId == sellerId 이고 otherUserId == buyerId 라고 가정
        Long otherUserId = 2L;

        Map<Long, Integer> unreadCountsMap = Map.of(userId, 0, otherUserId, 0);
        Map<Long, Boolean> statusMap = Map.of(userId, true, otherUserId, true);

        //TODO 대표이미지, 낙찰가는 아직 product 테이블에 반영x
        int successfulBid = 100000;

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 물건이 존재하지않습니다."));

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.create(userId, otherUserId, productId,
                "productImage", productEntity.getTitle(), successfulBid);

        chatRoomRepository.save(chatRoomEntity);

        CreateChatRoomResponse response = new CreateChatRoomResponse(chatRoomEntity.getId().toString());
        return response;
    }


    public void exitChatRoom(Long userId, ObjectId roomId) {
        //TODO 커스텀 예외 처리
        try {
            Query query = new Query(Criteria.where("_id").is(roomId));
            Update update = new Update().set("userStatus." + userId, false);

            mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);
        } catch (Exception e) {
            throw new RuntimeException("채팅방에서 나가기 중 예외 발생");
        }
    }


    public void resetUnreadCount(ObjectId roomObjectId, Long userId) {
        //TODO 커스텀 예외처리
        try {
            Query query = new Query(Criteria.where("_id").is(roomObjectId));
            Update update = new Update().set("unreadCounts." + userId, 0);
            UpdateResult result = mongoTemplate.updateFirst(query, update, ChatRoomEntity.class);

            if (result.getMatchedCount() == 0) {
                throw new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("안 읽은 메시지 카운트 초기화 중 오류가 발생", e);
        }
    }

    public ChatRoomBasicInfo getChatRoomBasicInfo(ObjectId roomObjectId, Long userId) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomObjectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다."));


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
                isSeller
        );

    }


    // 상대방 프로필 확인
    private OtherProfile getOtherProfile(ChatRoomEntity chatRoomEntity, Long userId) {
        Long otherUserId = userId == chatRoomEntity.getSellerId()
                ? chatRoomEntity.getBuyerId()
                : chatRoomEntity.getSellerId();

        UserEntity userEntity = userRepository.findById(otherUserId) //TODO 추후 예외처리
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        return new OtherProfile(userEntity.getNickname(), userEntity.getImageUrl(), userEntity.getId());

    }


}






