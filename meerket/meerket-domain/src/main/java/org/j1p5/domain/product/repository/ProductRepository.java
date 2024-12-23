package org.j1p5.domain.product.repository;

import java.util.List;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.querydsl.ProductRepositoryCustom;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    List<ProductEntity> findByUser(UserEntity user);
}
