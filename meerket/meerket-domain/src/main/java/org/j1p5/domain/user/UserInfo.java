package org.j1p5.domain.user;

import org.j1p5.domain.user.entity.UserEntity;

public record UserInfo(Long pk, String nickname, String imageUrl, String role, Long activityAreaId) {
    public static UserInfo of(UserEntity user, Long activityAreaId) {
        return new UserInfo(
                user.getId(),
                user.getNickname(),
                user.getImageUrl(),
                user.getRole().name(),
                activityAreaId
        );
    }
}
