package org.j1p5.api.comment.exception;

import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.common.exception.BaseErrorCode;

public class CommentNotFoundException extends WebException {
    public CommentNotFoundException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
