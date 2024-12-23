package org.j1p5.api.chat.service;

import static org.j1p5.api.chat.exception.ChatException.*;
import static org.j1p5.api.product.exception.ProductException.PRODUCT_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.OtherProfile;
import org.j1p5.api.chat.dto.response.CreateChatRoomResponse;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.chat.entity.ChatRoomEntity;
import org.j1p5.domain.chat.repository.ChatRoomRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

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

    /**
     * 특정 채팅방에 유저가 속해있는지 확인
     *
     * @param userId
     * @param roomId
     */
    public void verifyAccess(Long userId, ObjectId roomId) {
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

    // 상대방 프로필 확인
    public OtherProfile getOtherProfile(ChatRoomEntity chatRoomEntity, Long userId) {
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
