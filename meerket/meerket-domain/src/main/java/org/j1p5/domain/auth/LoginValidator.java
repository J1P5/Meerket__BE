package org.j1p5.domain.auth;

import org.j1p5.domain.auth.exception.InvalidProviderException;
import org.j1p5.domain.user.entity.Provider;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.j1p5.domain.global.exception.DomainErrorCode.INVALID_PROVIDER;

@Component
public class LoginValidator {

    public void validator(Provider socialProvider) {
        boolean isValid = Arrays.asList(Provider.values()).contains(socialProvider);

        if (!isValid) {
            throw new InvalidProviderException(INVALID_PROVIDER);
        }
    }
}
