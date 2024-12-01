package org.j1p5.infrastructure.global.config;

import static org.j1p5.infrastructure.global.exception.InfraErrorCode.*;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.j1p5.infrastructure.global.exception.OtherServerBadCredentialException;
import org.j1p5.infrastructure.global.exception.OtherServerBadRequestException;
import org.j1p5.infrastructure.global.exception.OtherServerInvalidAuthorizationException;
import org.j1p5.infrastructure.global.exception.OtherServerUnknownException;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            switch (response.status()) {
                case 400:
                    return new OtherServerBadRequestException(BAD_REQUEST);
                case 401:
                    return new OtherServerBadCredentialException(BAD_CREDENTIAL);
                case 403:
                    return new OtherServerInvalidAuthorizationException(INVALID_AUTHORIZATION);
                default:
                    return new OtherServerUnknownException(UNKNOWN_ERROR);
            }
        }

        return FeignException.errorStatus(methodKey, response);
    }
}
