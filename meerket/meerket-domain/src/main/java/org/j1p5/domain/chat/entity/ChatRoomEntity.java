package org.j1p5.domain.chat.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_rooms")
public class ChatRoomEntity {

    @Id
    private ObjectId id;
    private Long sellerId;
    private Long buyerId;
    private Long productId;
    private String productImage;
    private String productTitle;
    private int price;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;
    private Map<Long, Integer> unreadCounts;
    private Map<Long, Boolean> userStatus;
    private boolean isChatAvailable;


    public static ChatRoomEntity create(Long sellerId, Long buyerId, Long productId, String productImage,
                                        String productTitle, int price) {
        Map<Long, Integer> unreadMap = Map.of(sellerId, 0, buyerId, 0);
        Map<Long, Boolean> statusMap = Map.of(sellerId, true, buyerId, true);

        ChatRoomEntity entity = new ChatRoomEntity();
        entity.sellerId = Objects.requireNonNull(sellerId, "sellerId는 필수입니다.");
        entity.buyerId = Objects.requireNonNull(buyerId, "buyerId는 필수입니다.");
        entity.productId = Objects.requireNonNull(productId, "productId는 필수입니다.");
        entity.productImage = productImage;
        entity.productTitle = Objects.requireNonNull(productTitle);
        entity.price = price;
        entity.lastMessage = "거래 상대방과 대화해보세요 :)";
        entity.lastMessageAt = LocalDateTime.now();
        entity.createdAt = LocalDateTime.now();
        entity.unreadCounts = unreadMap;
        entity.userStatus = statusMap;
        entity.isChatAvailable = true;

        return entity;
    }

}
