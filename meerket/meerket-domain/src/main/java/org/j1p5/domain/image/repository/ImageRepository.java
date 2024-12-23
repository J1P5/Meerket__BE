package org.j1p5.domain.image.repository;

import java.util.List;
import org.j1p5.domain.image.entitiy.ImageEntity;
import org.j1p5.domain.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByProduct(ProductEntity product);
}
