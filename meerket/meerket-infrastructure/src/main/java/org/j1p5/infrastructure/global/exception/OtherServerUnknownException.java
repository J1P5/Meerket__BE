package org.j1p5.infrastructure.global.exception;

import org.j1p5.common.exception.BaseErrorCode;

public class OtherServerUnknownException extends InfraException {
    public OtherServerUnknownException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
