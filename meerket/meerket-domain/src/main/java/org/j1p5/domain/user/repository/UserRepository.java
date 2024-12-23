package org.j1p5.domain.user.repository;

import java.util.Optional;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositoryCustom {
    Optional<UserEntity> findBySocialIdAndProvider(String socialId, Provider provider);

    boolean existsByNickname(String nickname);
}
