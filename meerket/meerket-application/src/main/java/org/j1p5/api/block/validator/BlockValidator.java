package org.j1p5.api.block.validator;

import static org.j1p5.api.global.excpetion.WebErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.user.exception.UserNotFoundException;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockValidator {

    private final UserRepository userRepository;

    public UserEntity validateUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }
}
