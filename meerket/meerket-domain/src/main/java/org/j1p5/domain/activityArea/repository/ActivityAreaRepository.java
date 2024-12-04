package org.j1p5.domain.activityArea.repository;

import org.j1p5.domain.activityArea.entity.ActivityArea;
import org.j1p5.domain.activityArea.repository.querydsl.ActivityAreaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityAreaRepository
        extends JpaRepository<ActivityArea, Long>, ActivityAreaRepositoryCustom {}
