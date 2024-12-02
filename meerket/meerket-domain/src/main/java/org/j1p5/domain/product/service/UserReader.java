package org.j1p5.domain.product.service;

import static org.j1p5.common.exception.GlobalErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.j1p5.common.exception.CustomException;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserReader {
    private final UserRepository userRepository;

    public UserEntity getUser(String email) {
        UserEntity userEntity =
                userRepository
                        .findBySocialEmail(email)
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                USER_NOT_FOUND, "user not found")); // 유저 찾음직

        return userEntity;
    }
}
