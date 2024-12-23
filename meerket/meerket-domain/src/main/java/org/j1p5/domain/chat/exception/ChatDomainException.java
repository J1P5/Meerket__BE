package org.j1p5.domain.chat.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum ChatDomainException implements BaseErrorCode {
    NOT_FOUND_CHATROOM(404, "CHAT_ROOM_404", "채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_UPDATE_FAIL(500, "CHAT_ROOM_500", "채팅방 업데이트에 실패하였습니다."),
    CHAT_READ_ERROR(500, "CHAT_READ_500", "채팅을 조회하는 도중 에러가 발생하였습니다."),
    CHAT_EXIT_ERROR(500, "CHAT_EXIT_500", "채팅방에서 나가는 도중 에러가 발생하였습니다."),
    ;

    private final int status;
    private final String errorCode;
    private final String message;

    ChatDomainException(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}
