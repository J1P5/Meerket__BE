package org.j1p5.domain.user.repository;

import java.util.Optional;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findBySocialEmailAndProvider(String email, Provider provider);

    Optional<UserEntity> findBySocialEmail(String email);
}
