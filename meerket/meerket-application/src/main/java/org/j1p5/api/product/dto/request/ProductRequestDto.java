package org.j1p5.api.product.dto.request;

import lombok.Builder;
import org.j1p5.domain.product.entity.ProductCategory;
import org.j1p5.domain.product.entity.ProductEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.time.LocalDateTime;

@Builder
public record ProductRequestDto(
        String title,
        String content,
        int price,
        ProductCategory category,
        Double latitude,
        Double longtitude,
        String address,
        String location,
        LocalDateTime expiredTime
) {

    public ProductEntity toEntity(ProductRequestDto productRequestDto){

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),4326);
        Point coordinate = geometryFactory.createPoint(
                new Coordinate(productRequestDto.longtitude, productRequestDto.latitude));

        return ProductEntity.builder()
                .title(productRequestDto.title)
                .content(productRequestDto.content)
                .address(productRequestDto.address)
                .location(productRequestDto.location)
                .minPrice(productRequestDto.price)
                .category(productRequestDto.category)
                .coordinate(coordinate)
                .expiredTime(productRequestDto.expiredTime)
                .build();
    }

}
