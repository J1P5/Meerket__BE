package org.j1p5.domain.product.dto;

import lombok.Builder;
import org.j1p5.domain.product.entity.ProductCategory;
import org.j1p5.domain.product.entity.ProductEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.time.LocalDateTime;

@Builder
public record ProductUpdateInfo(
        String title,
        String content,
        Integer minimumPrice,
        ProductCategory category,
        LocalDateTime expiredTime,
        Double latitude,
        Double longtitude,
        String location
) {
}
