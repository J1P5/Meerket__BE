package org.j1p5.api.product.service;

import static org.j1p5.common.exception.GlobalErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.j1p5.common.exception.CustomException;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductUserReader {
    private final UserRepository userRepository;

    public UserEntity getUser(Long userId) {
        UserEntity userEntity =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                USER_NOT_FOUND, "user not found")); // 유저 찾음직
        return userEntity;
    }
}
