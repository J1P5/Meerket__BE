package org.j1p5.domain.areaAuth.repository;

import org.j1p5.domain.areaAuth.entity.AreaAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaAuthRepository extends JpaRepository<AreaAuthEntity, Integer> {
    boolean existsByUserId(Long userId);
}
