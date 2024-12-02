package org.j1p5.domain.auth;

import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.user.UserFixture;
import org.j1p5.domain.user.UserInfo;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.service.UserAppender;
import org.j1p5.domain.user.service.UserReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OauthServiceTest {

    @InjectMocks private OauthService oauthService;

    @Mock private OauthSender oauthSender;

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
        UserInfo userInfo = oauthService.login(code, provider);

        // then
        verify(userAppender, times(0)).append(profile, provider);
        assertEquals(user.getId(), userInfo.pk());
        assertEquals(user.getRole().name(), userInfo.role());
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
        UserInfo userInfo = oauthService.login(code, provider);

        // then
        verify(userAppender).append(profile, provider);
        assertEquals(user.getId(), userInfo.pk());
    }
}
