package org.j1p5.api.chat.service.usecase;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.j1p5.api.chat.dto.ChatRoomBasicInfo;
import org.j1p5.api.chat.dto.OtherProfile;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.chat.dto.response.ChatRoomEnterResponse;
import org.j1p5.api.chat.service.ChatMessageService;
import org.j1p5.api.chat.service.ChatRoomService;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.api.product.exception.ProductException;
import org.j1p5.api.product.service.EmdNameReader;
import org.j1p5.domain.chat.entity.ChatRoomEntity;
import org.j1p5.domain.chat.repository.ChatRoomRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.j1p5.api.chat.exception.ChatException.NOT_FOUND_CHATROOM;

/**
 * @author yechan
 * 특정 채팅방에 입장했을때 여러 채팅 정보들 제공
 */
@Service
@RequiredArgsConstructor
public class EnterChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;


    public ChatRoomEnterResponse execute(Long userId, String roomId) {

        ObjectId roomObjectId = chatRoomService.validateRoomId(roomId);

        chatRoomRepository.resetUnreadCount(roomObjectId, userId);

        List<ChatMessageResponse> chatMessages = chatMessageService.getChatMessages(roomObjectId, null);

        ChatRoomBasicInfo chatRoomBasicInfo = getChatRoomBasicInfo(roomObjectId, userId);

        return new ChatRoomEnterResponse(chatRoomBasicInfo, chatMessages);
    }


    private ChatRoomBasicInfo getChatRoomBasicInfo(ObjectId roomObjectId, Long userId) {
        ChatRoomEntity chatRoomEntity =
                chatRoomRepository
                        .findById(roomObjectId)
                        .orElseThrow(() -> new WebException(NOT_FOUND_CHATROOM));

        ProductEntity productEntity = productRepository.findById(chatRoomEntity.getProductId())
                .orElseThrow(() -> new WebException(ProductException.PRODUCT_NOT_FOUND));

        OtherProfile otherProfile = chatRoomService.getOtherProfile(chatRoomEntity, userId);
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
                chatRoomEntity.isChatAvailable(),
                EmdNameReader.getEmdName(productEntity.getUser()),
                productEntity.getCreatedAt(),
                productEntity.getStatus()
        );
    }

}
