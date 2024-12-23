package org.j1p5.domain.user;

import org.j1p5.domain.activityArea.dto.SimpleAddress;
import org.j1p5.domain.user.entity.UserEntity;

public record UserProfile(String nickname, String imageUrl, String ActivityEmdName) {
    public static UserProfile of(UserEntity userEntity, SimpleAddress activityEmdAddress) {
        if (userEntity == null && activityEmdAddress == null) {
            return new UserProfile(null, null, null);
        }
        if (userEntity == null) {
            return new UserProfile(null, null, activityEmdAddress.emdName());
        }
        if (activityEmdAddress == null) {
            return new UserProfile(userEntity.getNickname(), userEntity.getImageUrl(), null);
        }
        return new UserProfile(
                userEntity.getNickname(), userEntity.getImageUrl(), activityEmdAddress.emdName());
    }
}
