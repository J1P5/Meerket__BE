package org.j1p5.domain.chat.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Document(collection = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity {

    @Id
    private ObjectId id;
    private ObjectId roomId;
    private Long senderId;
    private String content;
    private LocalDateTime createdAt;


    public static ChatMessageEntity create(ObjectId roomId, Long senderId, String content) {
        return new ChatMessageEntity(
                Objects.requireNonNull(roomId, "roomId는 필수입니다."),
                Objects.requireNonNull(senderId, "senderId는 필수입니다."),
                Objects.requireNonNull(content, "내용은 필수 입니다.")
        );
    }

    private ChatMessageEntity(ObjectId roomId, Long senderId, String content) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

}
