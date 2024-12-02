package org.j1p5.domain.auth.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.domain.global.exception.DomainException;

public class InvalidProviderException extends DomainException {
    public InvalidProviderException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
