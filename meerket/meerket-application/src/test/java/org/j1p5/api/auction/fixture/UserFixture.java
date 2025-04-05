package org.j1p5.api.auction.fixture;

import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.Role;
import org.j1p5.domain.user.entity.UserEntity;

import java.lang.reflect.Field;

public class UserFixture {

    public static UserEntity withNo() {
        UserEntity user = UserEntity.create("socialId", Provider.KAKAO, Role.USER);

        return user;
    }




    public static UserEntity withId(Long id) {
        UserEntity user = UserEntity.create("socialId", Provider.KAKAO, Role.USER);

        try {
            Field field = UserEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return user;
    }
}
