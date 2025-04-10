package org.j1p5.api.auction.fixture;

import org.j1p5.domain.product.entity.ProductCategory;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.entity.ProductStatus;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.j1p5.api.global.converter.PointConverter.createPoint;

public class ProductFixture {

    public static ProductEntity withNo() {
        return ProductEntity.builder()
                .title("테스트 상품")
                .content("테스트 상품 설명")
                .minPrice(2000)
                .expiredTime(LocalDateTime.now().plusHours(2))
                .category(ProductCategory.CLOTHING)
                .address("서울시 강남구")
                .location("서울시 강남구")
                .coordinate(createPoint(37.4979, 127.0276))
                .status(ProductStatus.IN_PROGRESS)
                .user(null)
                .build();
    }


    public static ProductEntity withId(Long productId) {
        ProductEntity product =  ProductEntity.builder()
                .title("테스트 상품")
                .content("테스트 상품 설명")
                .minPrice(2000)
                .expiredTime(LocalDateTime.now().plusHours(2))
                .category(ProductCategory.CLOTHING)
                .address("서울시 강남구")
                .location("서울시 강남구")
                .coordinate(createPoint(37.4979, 127.0276))
                .status(ProductStatus.IN_PROGRESS)
                .user(null)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            Field field = ProductEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(product, productId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return product;
    }





}
