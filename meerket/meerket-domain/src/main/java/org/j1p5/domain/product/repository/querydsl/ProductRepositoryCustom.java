package org.j1p5.domain.product.repository.querydsl;

import java.util.List;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.entity.ProductStatus;
import org.locationtech.jts.geom.Point;

public interface ProductRepositoryCustom {

    List<ProductEntity> findProductsByCursor(
            List<Long> blockUserIds, Point coordinate, Long cursor, Integer size);

    List<ProductEntity> findCompletedProductsByCursor(Point coordinate, Long cursor, Integer size);

    List<ProductEntity> findProductByCategory(
            Point coordinate, String category, Long cursor, Integer size);

    List<ProductEntity> findProductByUserId(
            Long userId, Long cursor, Integer size, ProductStatus status);

    List<ProductEntity> findProductByKeyword(
            Point coordinate, String keyword, Long cursor, Integer size);
}
