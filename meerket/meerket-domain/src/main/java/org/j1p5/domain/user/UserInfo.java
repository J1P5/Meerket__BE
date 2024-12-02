package org.j1p5.domain.user;

import lombok.Builder;
import org.j1p5.domain.user.entity.UserEntity;

@Builder
public record UserInfo(Long pk, String role) {
    public static UserInfo from(UserEntity user) {
        return UserInfo.builder().pk(user.getId()).role(user.getRole().name()).build();
    }
}
