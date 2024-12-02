package org.j1p5.api.user.exception;

import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.common.exception.BaseErrorCode;

public class UserNotFoundException extends WebException {
    public UserNotFoundException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}