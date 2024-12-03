package org.j1p5.domain.fcm.repository;

import java.util.Optional;
import org.j1p5.domain.fcm.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {

    Optional<FcmTokenEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
