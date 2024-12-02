package org.j1p5.domain.product.service;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class PointConverter {
    public static Point createPoint(Double longtitude, Double latitude) {
        Point coordinate = null;
        if (latitude != null || longtitude != null) {
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
            coordinate = geometryFactory.createPoint(new Coordinate(longtitude, latitude));
        }
        return coordinate;
    }
}
