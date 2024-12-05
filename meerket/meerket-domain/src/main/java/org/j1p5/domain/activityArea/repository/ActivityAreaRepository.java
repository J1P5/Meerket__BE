package org.j1p5.domain.activityArea.repository;

import org.j1p5.domain.activityArea.entity.ActivityArea;
import org.j1p5.domain.activityArea.repository.querydsl.ActivityAreaRepositoryCustom;
import org.j1p5.domain.user.entity.EmdArea;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityAreaRepository extends JpaRepository<ActivityArea, Long>, ActivityAreaRepositoryCustom {
    boolean existsByUser(UserEntity user);

    List<ActivityArea> findByUser(UserEntity user);

    void deleteByUserAndEmdArea(UserEntity user, EmdArea emdArea);
}
