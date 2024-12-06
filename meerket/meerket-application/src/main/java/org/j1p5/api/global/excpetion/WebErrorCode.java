package org.j1p5.api.global.excpetion;

import lombok.Getter;
import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

@Getter
public enum WebErrorCode implements BaseErrorCode {
    USER_NOT_FOUND(404, "USER404", "해당 유저가 존재하지 않습니다"),
    NICKNAME_ALREADY_EXIST(409, "USER409", "이미 존재하는 닉네임입니다."),

    EMD_AREA_NOT_FOUND(404, "EMD404", ""),

    ACTIVITY_AREA_ALREADY_EXIST(409, "ACTIVITY_AREA409", "등록한 동네가 이미 존재합니다."),
    ACTIVITY_AREA_NOT_FOUND(404, "ACTIVITY_AREA404", "활동지역 동네가 없습니다."),

    AREA_AUTH_NOT_FOUND(404, "AREA_AUTH404", "지역 인증 이력이 없습니다."),
    CURRENT_POINT_NOT_MATCH(404, "AREA_AUTH404", "현재 위치가 활동 지역과 다릅니다.")
    ;
    private final int status;
    private final String errorCode;
    private final String message;

    WebErrorCode(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}