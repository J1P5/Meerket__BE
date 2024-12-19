package org.j1p5.api.product.exception;

import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.common.exception.BaseErrorCode;

public class ProductNotFoundException extends WebException {
    public ProductNotFoundException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
