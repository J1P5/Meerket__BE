package org.j1p5.api.global.excpetion;

import lombok.extern.slf4j.Slf4j;
import org.j1p5.api.global.response.Response;
import org.j1p5.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> onCustomException(CustomException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getBaseErrorCode().getErrorResponse().getStatus())
                .body(
                        Response.onFailure(
                                e.getBaseErrorCode().getErrorResponse().getErrorCode(),
                                e.getBaseErrorCode().getErrorResponse().getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> onException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.onFailure("UNKNOWN500", "unknown server error"));
    }
}
