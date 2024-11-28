package org.j1p5.domain.product.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.entity.QProductEntity;
import org.locationtech.jts.geom.Point;

import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ProductRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QProductEntity qProduct = QProductEntity.productEntity;

    @Override
    public List<ProductEntity> findProductsByCursor(Point coordinate, Long cursor, Integer size) {
        // QueryDSL을 사용한 커서 기반 조회
        return queryFactory
                .selectFrom(qProduct)
                .where(
                        withinDistance(coordinate,100_000), // 거리 조건 (100km 이내)
                        cursorCondition(cursor) // 커서 조건
                )
                .orderBy(qProduct.id.desc()) // 내림차순 정렬
                .limit(size) // 페이지 크기 제한
                .fetch();
    }

    /**
     * 특정 좌표와 반경 내의 상품만 조회하는 조건 생성
     *
     * @param coordinate 기준 좌표
     * @param distance   반경 (단위: 미터)
     * @return BooleanExpression (QueryDSL 조건)
     */
    private BooleanExpression withinDistance(Point coordinate, int distance) {
        String geoFunction = "ST_Distance_Sphere({0}, {1})";
        return Expressions.numberTemplate(Double.class, geoFunction, qProduct.coordinate, coordinate)//상품 좌표 , 사용자 활동좌표
                .loe((double) distance);
    }

    /**
     * 커서를 기준으로 특정 상품 ID보다 작은 상품만 조회하는 조건 생성
     *
     * @param cursor 마지막으로 조회한 상품 ID
     * @return BooleanExpression (QueryDSL 조건)
     */
    private BooleanExpression cursorCondition(Long cursor) {
        if (cursor == null) {
            return null; // 커서가 없으면 조건 생략
        }
        return qProduct.id.lt(cursor);
    }
}
