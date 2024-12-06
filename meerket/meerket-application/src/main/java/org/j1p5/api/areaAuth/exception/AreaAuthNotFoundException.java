package org.j1p5.api.areaAuth.exception;

import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.common.exception.BaseErrorCode;

public class AreaAuthNotFoundException extends WebException {
    public AreaAuthNotFoundException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
