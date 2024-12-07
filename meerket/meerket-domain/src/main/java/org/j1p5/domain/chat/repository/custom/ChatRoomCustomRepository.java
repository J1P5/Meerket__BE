package org.j1p5.domain.chat.repository.custom;

import org.bson.types.ObjectId;
import org.j1p5.domain.chat.command.UpdateChatRoomCommand;

public interface ChatRoomCustomRepository {
    void resetUnreadCount(ObjectId roomObjectId, Long userId);

    void exitChatRoom(Long userId, ObjectId roomId);

    void updateChatRoomInfo(UpdateChatRoomCommand command);
}
