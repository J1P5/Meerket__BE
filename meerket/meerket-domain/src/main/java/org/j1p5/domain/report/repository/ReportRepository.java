package org.j1p5.domain.report.repository;

import org.j1p5.domain.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {}
