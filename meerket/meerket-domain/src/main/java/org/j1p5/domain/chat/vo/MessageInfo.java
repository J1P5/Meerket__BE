package org.j1p5.domain.chat.vo;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MessageInfo {

    private final Long userId;
    private final Long receiverId;
    private final String content;
    private final String roomId;

    public MessageInfo(Long userId, Long receiverId, String content, String roomId){
        this.userId = Objects.requireNonNull(userId, "userId는 필수입니다.");
        this.receiverId = Objects.requireNonNull(receiverId, "receiverId는 필수입니다.");
        this.content = Objects.requireNonNull(content, "content는 필수입니다.");
        this.roomId = Objects.requireNonNull(roomId, "roomId는 필수입니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageInfo that = (MessageInfo) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(receiverId, that.receiverId) &&
                Objects.equals(content, that.content) &&
                Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, receiverId, content, roomId);
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "userId=" + userId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
