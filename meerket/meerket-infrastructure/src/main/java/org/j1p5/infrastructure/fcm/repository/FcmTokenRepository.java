package org.j1p5.infrastructure.fcm.repository;

import org.j1p5.domain.fcm.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {

    Optional<FcmTokenEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
