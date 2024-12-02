package org.j1p5.infrastructure.global.exception;

import org.j1p5.common.exception.BaseErrorCode;

public class OtherServerInvalidAuthorizationException extends InfraException {
    public OtherServerInvalidAuthorizationException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
