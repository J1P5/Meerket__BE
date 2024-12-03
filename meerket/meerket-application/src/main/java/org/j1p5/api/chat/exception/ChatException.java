package org.j1p5.api.chat.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum ChatException implements BaseErrorCode {

    // 채팅관련 에러
    RECEIVER_NOT_FOUND(404, "RECEIVER404", "상대방이 존재하지 않습니다."),
    CHAT_SAVE_ERROR(500,"CHATSAVE500", "채팅 저장에 실패하였습니다."),
    NOT_FOUND_CHATROOM(404, "CHATROOM404", "채팅방을 찾을 수 없습니다."),
    NOT_MEMBER_OF_CHATROOM(403, "CHATROOM403", "채팅방에 속하지 않은 유저입니다."),
    INVALID_ROOM_ID(400, "ROOMID400", "roomId 형식이 잘못되었습니다."),
    CHAT_ROOM_UPDATE_FAIL(500, "CHATROOM500", "채팅방 업데이트에 실패하였습니다."),
    INVALID_ROOM_TYPE(400, "CHATROOM400", "입력된 채팅방 타입이 잘못되었습니다."),
    CHATROOM_LIST_ERROR(500, "CHATROOM500", "채팅방 목록 조회에 실패하였습니다."),
    CHAT_EXIT_ERROR(500, "CHATEXIT500", "채팅방에서 나가는 도중 에러가 발생하였습니다."),






    ;

    private final int status;
    private final String errorCode;
    private final String message;

    ChatException(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }


    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}
