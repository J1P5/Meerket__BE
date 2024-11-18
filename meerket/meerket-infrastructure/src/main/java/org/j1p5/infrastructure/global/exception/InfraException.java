package org.j1p5.infrastructure.global.exception;

import lombok.Getter;
import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.CustomException;

@Getter
public class InfraException extends CustomException {

    public InfraException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode, "infra layer");
    }
}
