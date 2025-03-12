package org.j1p5.domain.product.dto;

import org.locationtech.jts.geom.Point;

public record ProductLocationInfo(
        double longitude,
        double latitude,
        String address, // ~ 동 ~구 까지표기한 주소
        String location // 상세주소
        ) {
    public static ProductLocationInfo of(Point coordinate, String address, String location) {
        return new ProductLocationInfo(coordinate.getX(), coordinate.getY(), address, location);
    }
}
