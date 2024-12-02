package org.j1p5.api.comment.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum CommentErrorCode implements BaseErrorCode {

    COMMENT_NOT_FOUND(404,"COMMENT404","댓글을 조회할 수 없습니다."),
    COMMENT_DEPTH_EXCEEDED(405,"COMMENT405", "대댓글 이상의 댓글 생성은 허용되지 않습니다.")

    ;
    private final int status;
    private final String errorCode;
    private final String message;

    CommentErrorCode(int status, String errorCode, String message){
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false,status,errorCode,message);
    }
}
