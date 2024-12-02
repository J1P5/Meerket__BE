package org.j1p5.infrastructure.global.exception;

import org.j1p5.common.exception.BaseErrorCode;

public class OtherServerBadRequestException extends InfraException {
    public OtherServerBadRequestException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
