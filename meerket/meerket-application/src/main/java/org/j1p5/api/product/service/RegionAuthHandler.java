package org.j1p5.api.product.service;

import static org.j1p5.api.product.exception.ProductException.REGION_AUTH_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.areaAuth.repository.AreaAuthRepository;
import org.j1p5.domain.global.exception.DomainException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegionAuthHandler {

    private final AreaAuthRepository areaAuthRepository;

    public void checkAuth(Long userId) {
        if (!(areaAuthRepository.existsByUserId(userId))) {
            throw new DomainException(REGION_AUTH_NOT_FOUND);
        }
    }
}
