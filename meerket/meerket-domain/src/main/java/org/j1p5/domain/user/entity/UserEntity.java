package org.j1p5.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.j1p5.domain.global.entity.BaseEntity;

import java.util.List;

@Entity(name = "user")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "social_id", nullable = false)
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
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<ActivityArea> activityAreas;
}
