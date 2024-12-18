package org.j1p5.domain.areaAuth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.j1p5.domain.user.entity.EmdArea;
import org.j1p5.domain.user.entity.UserEntity;

@Entity(name = "area_authentication")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class AreaAuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timeStamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emd_id")
    private EmdArea emdArea;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;

    private AreaAuthEntity(LocalDateTime timeStamp, UserEntity user, EmdArea emdArea) {
        this.timeStamp = timeStamp;
        this.user = user;
        this.emdArea = emdArea;
    }

    public static AreaAuthEntity of(LocalDateTime timeStamp, UserEntity user, EmdArea emdArea) {
        return new AreaAuthEntity(timeStamp, user, emdArea);
    }

    public void updateHistory() {
        this.timeStamp = LocalDateTime.now();
    }

    public void updateEmdArea(EmdArea emdArea) {
        this.emdArea = emdArea;
    }


}
