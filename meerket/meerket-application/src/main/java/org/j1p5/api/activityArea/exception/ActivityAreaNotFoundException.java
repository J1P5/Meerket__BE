package org.j1p5.api.activityArea.exception;

import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.common.exception.BaseErrorCode;

public class ActivityAreaNotFoundException extends WebException {
    public ActivityAreaNotFoundException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
