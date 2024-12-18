package org.j1p5.domain.activityArea.entity;

import jakarta.persistence.*;
import lombok.*;
import org.j1p5.domain.user.entity.EmdArea;
import org.j1p5.domain.user.entity.UserEntity;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class ActivityArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "range_level")
    private Short rangeLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emd_id")
    private EmdArea emdArea;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;

    private ActivityArea(UserEntity user, EmdArea emdArea) {
        this.user = user;
        this.emdArea = emdArea;
    }

    public static ActivityArea create(UserEntity user, EmdArea emdArea) {
        return new ActivityArea(user, emdArea);
    }

    public void updateEmdArea(EmdArea emdArea) {
        this.emdArea = emdArea;
    }
}
