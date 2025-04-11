package org.j1p5.api.fcm.exception;

import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.common.exception.BaseErrorCode;

public class FcmRedirectException extends WebException {
    public FcmRedirectException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
