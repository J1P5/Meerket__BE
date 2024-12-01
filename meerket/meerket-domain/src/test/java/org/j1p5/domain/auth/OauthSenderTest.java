// package org.j1p5.domain.auth;
//
// import org.j1p5.domain.auth.dto.OauthProfile;
// import org.j1p5.domain.auth.dto.OauthToken;
// import org.j1p5.domain.user.entity.Provider;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import java.util.List;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.when;
//
// @ExtendWith(MockitoExtension.class)
// public class OauthSenderTest {
//
//    private OauthSender oauthSender;
//
//    @Mock private OauthClient oauthClient;
//
//    @BeforeEach
//    void setUp() {
//        when(oauthClient.getProvider()).thenReturn(Provider.KAKAO);
//
//        List<OauthClient> clients = List.of(oauthClient);
//        oauthSender = new OauthSender(clients);
//    }
//
//    @Test
//    @DisplayName("외부 플랫폼(카카오 활용) 프로필 정보 요청 테스트")
//    void requestSuccess() {
//        String code = "auth code";
//        String provider = "KAKAO";
//        OauthToken token = new KakaoToken();
//        OauthProfile profile = AuthFixture.create();
//
//        // given
//        when(oauthClient.getOauthToken(anyString())).thenReturn(token);
//        when(oauthClient.getOauthProfile(token.getAccessToken())).thenReturn(profile);
//
//        // when
//        OauthProfile actual = oauthSender.request(code, provider);
//
//        // then
//        assertThat(profile).isEqualTo(actual);
//    }
// }
