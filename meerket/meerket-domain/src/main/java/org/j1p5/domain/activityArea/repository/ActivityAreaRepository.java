package org.j1p5.domain.activityArea.repository;

import java.util.List;
import java.util.Optional;
import org.j1p5.domain.activityArea.entity.ActivityArea;
import org.j1p5.domain.activityArea.repository.querydsl.ActivityAreaRepositoryCustom;
import org.j1p5.domain.user.entity.EmdArea;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityAreaRepository
        extends JpaRepository<ActivityArea, Long>, ActivityAreaRepositoryCustom {
    boolean existsByUser(UserEntity user);

    List<ActivityArea> findAllByUser(UserEntity user);

    Optional<ActivityArea> findByUser(UserEntity user);

    void deleteByUserAndEmdArea(UserEntity user, EmdArea emdArea);
}
