package org.j1p5.domain.report.repository;

import java.util.List;
import org.j1p5.domain.report.entity.ReportEntity;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByUser(UserEntity user);
}
