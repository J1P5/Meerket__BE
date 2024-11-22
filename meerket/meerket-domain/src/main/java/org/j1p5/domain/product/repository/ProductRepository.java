package org.j1p5.domain.product.repository;

import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.querydsl.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity,Long>, ProductRepositoryCustom {
}
