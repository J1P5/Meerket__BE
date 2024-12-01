package org.j1p5.domain.auth.validator;

import static org.j1p5.domain.global.exception.DomainErrorCode.INVALID_PROVIDER;

import java.util.Arrays;
import org.j1p5.domain.auth.exception.InvalidProviderException;
import org.j1p5.domain.user.entity.Provider;
import org.springframework.stereotype.Component;

@Component
public class LoginValidator {

    public void validator(String socialProvider) {
        boolean isValid =
                Arrays.asList(Provider.values()).stream()
                        .map(Provider::name)
                        .anyMatch(name -> name.equals(socialProvider));

        if (!isValid) {
            throw new InvalidProviderException(INVALID_PROVIDER);
        }
    }
}
