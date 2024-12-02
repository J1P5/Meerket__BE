package org.j1p5.domain.product.repository.querydsl;

import org.j1p5.common.dto.Cursor;
import org.j1p5.domain.product.entity.ProductEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductRepositoryCustom {

    List<ProductEntity> findProductsByCursor(Point coordinate, Long cursor, Integer size);
}
