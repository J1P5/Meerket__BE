package org.j1p5.domain.fcm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.j1p5.domain.global.entity.BaseEntity;
import org.j1p5.domain.user.entity.UserEntity;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fcm_tokens")
public class FcmTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;


    @Column(nullable = false, unique = true, length = 512)
    private String token;

    public void updateToken(String newToken) {
        this.token = newToken;
    }

    public static FcmTokenEntity create(UserEntity user, String token) {
        FcmTokenEntity fcmTokenEntity = new FcmTokenEntity();
        fcmTokenEntity.user = user;
        fcmTokenEntity.token = token;

        return fcmTokenEntity;
    }

}
