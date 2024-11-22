package org.j1p5.domain.auth.area.repository;

import org.j1p5.domain.auth.area.entity.AreaAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaAuthRepository extends JpaRepository<AreaAuthEntity, Long> {
}
