package org.j1p5.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final BaseErrorCode baseErrorCode;
    private final String layer;

    public CustomException(BaseErrorCode baseErrorCode, String layer) {
        this.baseErrorCode = baseErrorCode;
        this.layer = layer;
    }

    public String getMessage() {
        if (layer == null) {
            return baseErrorCode.getErrorResponse().getMessage();
        }
        return String.format("%s - %s", layer, baseErrorCode.getErrorResponse().getMessage());
    }

}
