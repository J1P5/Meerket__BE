package api.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 성공 여부, 응답 코드, 응답 메세지, 응답 데이터 공통 응답 반환
 * @author Icecoff22
 * @param <T>
 */
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
@Getter
public class Response<T> {
    private boolean isSuccess;
    private String code;
    private String message;
    private T result;

    private Response(boolean isSuccess, String code, String message, T result) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    private Response(boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    private Response(boolean isSuccess, T result) {
        this.isSuccess = isSuccess;
        this.result = result;
    }

    private Response(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /* 응답 성공, 응답 데이터 o */
    public static <T> Response<T> onSuccess(T result) {
        return new Response<>(true, result);
    }

    /* 응답 성공, 응답 데이터 x */
    public static Response<Void> onSuccess() {
        return new Response<>(true);
    }

    /* 응답 실패, 응답 데이터 o */
    public static <T> Response<T> onFailure(String code, String message, T result) {
        return new Response<>(false, code, message, result);
    }

    /* 응답 실패, 응답 데이터 x */
    public static Response<Void> onFailure(String code, String message) {
        return new Response<>(false, code, message);
    }
}
