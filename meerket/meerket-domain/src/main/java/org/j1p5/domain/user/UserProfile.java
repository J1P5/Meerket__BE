package org.j1p5.domain.user;

import org.j1p5.domain.activityArea.dto.SimpleAddress;
import org.j1p5.domain.user.entity.UserEntity;

public record UserProfile(String nickname, String imageUrl, String ActivityEmdName) {
    public static UserProfile of(UserEntity userEntity, SimpleAddress activityEmdAddress) {
        return new UserProfile(userEntity.getNickname(), userEntity.getImageUrl(), activityEmdAddress.emdName());
    }
}
