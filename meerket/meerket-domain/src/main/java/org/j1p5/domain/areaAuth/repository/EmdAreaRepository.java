package org.j1p5.domain.areaAuth.repository;

import org.j1p5.domain.areaAuth.entity.AreaAuthEntity;
import org.j1p5.domain.areaAuth.repository.custom.AreaAuthRepositoryCustom;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaAuthRepository extends JpaRepository<AreaAuthEntity, Integer>, AreaAuthRepositoryCustom {
    boolean existsByUserId(Long userId);
    Optional<AreaAuthEntity> findByUser(UserEntity user);
}
