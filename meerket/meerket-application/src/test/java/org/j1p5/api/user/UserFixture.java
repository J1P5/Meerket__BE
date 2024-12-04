package org.j1p5.api.user;

import java.lang.reflect.Field;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.Role;
import org.j1p5.domain.user.entity.UserEntity;

public class UserFixture {
    public static UserEntity createUserWithId() {
        UserEntity user = UserEntity.create("1", "test@test.com", Provider.KAKAO, Role.USER);

        try {
            Field field = UserEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, 1L);
        } catch (Exception ignored) {
        }

        return user;
    }
}
