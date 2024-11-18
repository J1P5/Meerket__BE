package org.j1p5.domain.global.exception;

import lombok.Getter;
import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.CustomException;

@Getter
public class DomainException extends CustomException {

    public DomainException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode, "domain layer");
    }
}
