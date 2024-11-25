package org.j1p5.domain.auth;

import lombok.Getter;

@Getter
public class NaverProfile implements OauthProfile {
    private String id;
    private String email;
}
