package org.j1p5.domain.product.service;

import static org.j1p5.domain.product.exception.ProductException.REGION_AUTH_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.auth.area.repository.AreaAuthRepository;
import org.j1p5.domain.global.exception.DomainException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegionauth {

    private final AreaAuthRepository areaAuthRepository;

    public boolean checkAuth(Long userId) {
        if (!(areaAuthRepository.existsByUserId(userId))) {
            throw new DomainException(REGION_AUTH_NOT_FOUND);
        }
        return true;
    }
}
