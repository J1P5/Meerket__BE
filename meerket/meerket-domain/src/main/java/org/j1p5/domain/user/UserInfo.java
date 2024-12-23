package org.j1p5.domain.user;

import org.j1p5.domain.activityArea.dto.SimpleAddress;
import org.j1p5.domain.user.entity.UserEntity;

public record UserInfo(
        Long pk, String nickname, String imageUrl, String role, SimpleAddress activityAreaInfo) {
    public static UserInfo of(UserEntity user, SimpleAddress activityAreaInfo) {
        return new UserInfo(
                user.getId(),
                user.getNickname(),
                user.getImageUrl(),
                user.getRole().name(),
                activityAreaInfo);
    }
}
