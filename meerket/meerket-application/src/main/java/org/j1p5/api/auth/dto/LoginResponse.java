package org.j1p5.api.auth.dto;

import org.j1p5.domain.user.UserInfo;

public record LoginResponse(String nickname, String profileUrl, Integer emdId, String emdName) {
    public static LoginResponse from(UserInfo userInfo) {
        if (userInfo.activityAreaInfo() == null) {
            return new LoginResponse(userInfo.nickname(), userInfo.imageUrl(), null, null);
        }
        return new LoginResponse(
                userInfo.nickname(),
                userInfo.imageUrl(),
                userInfo.activityAreaInfo().emdId(),
                userInfo.activityAreaInfo().emdName());
    }
}
