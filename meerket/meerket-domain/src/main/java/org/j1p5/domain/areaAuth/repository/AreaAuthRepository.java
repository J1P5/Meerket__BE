package org.j1p5.domain.areaAuth.repository;

import java.util.Optional;
import org.j1p5.domain.areaAuth.entity.AreaAuthEntity;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.custom.EmdAreaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaAuthRepository
        extends JpaRepository<AreaAuthEntity, Integer>, EmdAreaRepositoryCustom {
    boolean existsByUserId(Long userId);

    Optional<AreaAuthEntity> findByUser(UserEntity user);
}
