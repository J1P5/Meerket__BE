package org.j1p5.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.j1p5.domain.global.entity.BaseEntity;

@Entity(name = "user")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "social_id", nullable = false, length = 50)
    private String socialId;

    @Column(name = "social_email", nullable = false, length = 50)
    private String socialEmail;

    @Column(name = "provider", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "nickname", length = 15)
    private String nickname;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private UserEntity(String SocialId, String SocialEmail, Provider Provider, Role role) {
        this.socialId = SocialId;
        this.socialEmail = SocialEmail;
        this.provider = Provider;
        this.role = role;
    }

    public static UserEntity create(String SocialId, String SocialEmail, Provider Provider, Role role) {
        return new UserEntity(SocialId, SocialEmail, Provider, role);
    }
}
