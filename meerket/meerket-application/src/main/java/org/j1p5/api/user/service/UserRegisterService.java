package org.j1p5.api.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.user.exception.NicknameAlreadyExistException;
import org.j1p5.api.user.exception.UserNotFoundException;
import org.j1p5.domain.user.UserImageClient;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.File;

import static org.j1p5.api.global.excpetion.WebErrorCode.NICKNAME_ALREADY_EXIST;
import static org.j1p5.api.global.excpetion.WebErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;
    private final UserImageClient imageClient;

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        nickNameValidation(nickname);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        user.updateNickname(nickname);
    }

    public void nickNameValidation(String nickname) {
        boolean isExist = userRepository.existsByNickname(nickname);

        if (isExist) {
            throw new NicknameAlreadyExistException(NICKNAME_ALREADY_EXIST);
        }
    }

    @Transactional
    public void updateProfile(Long userId, File imageFile) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NicknameAlreadyExistException(USER_NOT_FOUND));

        String uploadFileName = imageClient.upload(imageFile);

        user.updateProfile(uploadFileName);
    }
}