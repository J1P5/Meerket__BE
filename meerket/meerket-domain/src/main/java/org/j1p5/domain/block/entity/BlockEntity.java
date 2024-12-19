package org.j1p5.domain.block.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.j1p5.domain.global.entity.BaseEntity;
import org.j1p5.domain.user.entity.UserEntity;

@Entity(name = "block")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BlockEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_user_id")
    private UserEntity blockedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;

    private BlockEntity(UserEntity blockedUser, UserEntity user) {
        this.blockedUser = blockedUser;
        this.user = user;
    }

    public static BlockEntity create(UserEntity blockedUser, UserEntity user) {
        return new BlockEntity(blockedUser, user);
    }

    public void withdraw() {
        this.isDeleted = true;
    }
}
