package org.j1p5.api.global.excpetion;

import lombok.Getter;
import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.CustomException;

@Getter
public class WebException extends CustomException {

    public WebException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode, "web layer");
    }
}
