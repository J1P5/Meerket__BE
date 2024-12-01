//package org.j1p5.api.auth;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpSession;
//import org.j1p5.api.auth.dto.LoginRequest;
//import org.j1p5.domain.auth.OauthService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(value = AuthApiTest.class)
//public class AuthApiTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private OauthService oauthService;
//
//    @Autowired
//    private ObjectMapper mapper;
//    @Autowired
//    private HttpSession httpSession;
//
//    @Test
//    @DisplayName("소셜 로그인 성공 테스트")
//    void login_success() throws Exception {
//
//        //given
//        Long id = 1L;
//        String code = "auth code";
//        String provider = "KAKAO";
//        LoginRequest loginRequest = new LoginRequest(code, provider);
//
//        when(oauthService.login(code, provider)).thenReturn(id);
//
//        //when
//        ResultActions result = mvc.perform(post("/api/v1/oauth")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(loginRequest))
//        );
//
//        //then
//        result.andExpect(status().isOk());
//    }
//}
