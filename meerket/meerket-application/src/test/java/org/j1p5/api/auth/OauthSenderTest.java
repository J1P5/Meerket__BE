package org.j1p5.api.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import org.j1p5.api.auth.service.OauthSenderService;
import org.j1p5.domain.auth.OauthClient;
import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.auth.dto.OauthToken;
import org.j1p5.domain.user.entity.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OauthSenderTest {

    private OauthSenderService oauthSender;

    @Mock private OauthClient oauthClient;

    @BeforeEach
    void setUp() {
        when(oauthClient.getProvider()).thenReturn(Provider.KAKAO);

        List<OauthClient> clients = List.of(oauthClient);
        oauthSender = new OauthSenderService(clients);
    }

    @Test
    @DisplayName("외부 플랫폼(카카오 활용) 프로필 정보 요청 테스트")
    void requestSuccess() {
        String code = "auth code";
        String provider = "KAKAO";
        OauthToken token = OauthTokenFixture.create();
        OauthProfile profile = AuthFixture.create();

        // given
        when(oauthClient.getOauthToken(anyString())).thenReturn(token);
        when(oauthClient.getOauthProfile(token.getAccessToken())).thenReturn(profile);

        // when
        OauthProfile actual = oauthSender.request(code, provider);

        // then
        assertThat(profile).isEqualTo(actual);
    }
}
