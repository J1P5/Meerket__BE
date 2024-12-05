package org.j1p5.api.user.dto.response;

import org.j1p5.domain.user.UserProfile;

public record ProfileResponse(String nickname, String imageUrl, String activityEmdName) {
    public static ProfileResponse from(UserProfile userProfile) {
        return new ProfileResponse(userProfile.nickname(), userProfile.imageUrl(), userProfile.ActivityEmdName());
    }
}
