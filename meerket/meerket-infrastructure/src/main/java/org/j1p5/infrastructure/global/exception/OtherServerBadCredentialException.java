package org.j1p5.infrastructure.global.exception;

import org.j1p5.common.exception.BaseErrorCode;

public class OtherServerBadCredentialException extends InfraException {
    public OtherServerBadCredentialException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
