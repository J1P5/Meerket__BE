package org.j1p5.domain.report.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.j1p5.domain.global.entity.BaseEntity;
import org.j1p5.domain.user.entity.UserEntity;

@Entity(name = "report")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReportEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_type", nullable = false)
    private ReportType reportType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private ReportEntity(ReportType reportType, Long targetId, String content, UserEntity user) {
        this.reportType = reportType;
        this.targetId = targetId;
        this.content = content;
        this.user = user;
    }

    public static ReportEntity create(
            ReportType reportType, Long targetId, String content, UserEntity user) {
        return new ReportEntity(reportType, targetId, content, user);
    }

    public void withdraw() {
        this.isDeleted = true;
    }
}
