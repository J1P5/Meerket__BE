package org.j1p5.domain.user.entity;

import static org.j1p5.domain.global.exception.DomainErrorCode.INVALID_PROVIDER;

import java.util.Arrays;
import org.j1p5.domain.auth.exception.InvalidProviderException;

public enum Provider {
    KAKAO,
    NAVER;

    public static Provider get(String socialProvider) {
        boolean isValid =
                Arrays.stream(Provider.values())
                        .map(Provider::name)
                        .anyMatch(name -> name.equals(socialProvider));

        if (!isValid) {
            throw new InvalidProviderException(INVALID_PROVIDER);
        }

        return Provider.valueOf(socialProvider);
    }
}
