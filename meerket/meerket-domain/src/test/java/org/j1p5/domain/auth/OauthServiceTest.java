package org.j1p5.domain.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.auth.exception.InvalidProviderException;
import org.j1p5.domain.auth.validator.LoginValidator;
import org.j1p5.domain.user.UserFixture;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.service.UserAppender;
import org.j1p5.domain.user.service.UserReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OauthServiceTest {

    @InjectMocks private OauthService oauthService;

    @Mock private OauthSender oauthSender;

    @Mock private LoginValidator loginValidator;

    @Mock private UserAppender userAppender;

    @Mock private UserReader userReader;

    @Test
    @DisplayName("기존 로그인 성공 테스트")
    void login_success() {
        // given
        String code = "auth code";
        String provider = "KAKAO";

        OauthProfile profile = AuthFixture.create();
        UserEntity user = UserFixture.createUserWithId();

        when(oauthSender.request(code, provider)).thenReturn(profile);
        when(userReader.read(anyString(), anyString())).thenReturn(user);

        // when
        Long id = oauthService.login(code, provider);

        // then
        verify(userAppender, times(0)).append(profile, provider);
        assertEquals(user.getId(), id);
    }

    @Test
    @DisplayName("첫 로그인 성공 테스트")
    void first_login_success() {
        // given
        String code = "auth code";
        String provider = "KAKAO";

        OauthProfile profile = AuthFixture.create();
        UserEntity user = UserFixture.createUserWithId();

        when(oauthSender.request(code, provider)).thenReturn(profile);
        when(userReader.read(anyString(), anyString())).thenReturn(null);
        when(userAppender.append(profile, provider)).thenReturn(user);

        // when
        Long id = oauthService.login(code, provider);

        // then
        verify(userAppender).append(profile, provider);
        assertEquals(user.getId(), id);
    }

    @Test
    @DisplayName("명시된 플랫폼과 다른 문자열 들어왔을 때 예외를 발생시킨다.")
    void invalid_provider() {
        // given
        String code = "auth code";
        String provider = "ERROR";

        // when
        doThrow(InvalidProviderException.class).when(loginValidator).validator(provider);

        // then
        assertThrows(InvalidProviderException.class, () -> oauthService.login(code, provider));
    }
}