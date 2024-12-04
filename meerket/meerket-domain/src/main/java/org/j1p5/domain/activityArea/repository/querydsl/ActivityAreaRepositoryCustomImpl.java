package org.j1p5.domain.activityArea.repository.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.j1p5.domain.activityArea.dto.ActivityAreaAddress;
import org.j1p5.domain.activityArea.entity.QActivityArea;
import org.j1p5.domain.user.entity.QEmdArea;
import org.j1p5.domain.user.entity.QSggArea;
import org.j1p5.domain.user.entity.QSidoArea;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ActivityAreaRepositoryCustomImpl implements ActivityAreaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ActivityAreaRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QActivityArea qActivityArea = QActivityArea.activityArea;
    QEmdArea qEmdArea = QEmdArea.emdArea;
    QSggArea qSggArea = QSggArea.sggArea;
    QSidoArea qSidoArea = QSidoArea.sidoArea;

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
        return qEmdArea.id.lt(cursor);
    }

    /**
     * 특정 좌표와 가까운 순서
     * @param location
     * @return OrderSpecifier
     */
    private OrderSpecifier<?> getOrderSpecifiersByDistance(Point location) {
        String geoFunction = "ST_Distance_Sphere(coordinate, {0})";
        return new OrderSpecifier<>(Order.ASC, Expressions.numberTemplate(Double.class, geoFunction, location));
    }

    @Override
    public Page<ActivityAreaAddress> getActivityAreas(Point coordinate, Pageable pageable) {
        List<ActivityAreaAddress> areaInfos =
                queryFactory
                        .selectDistinct(
                                Projections.constructor(
                                        ActivityAreaAddress.class,
                                        qEmdArea.id.as("emdId"),
                                        qSidoArea.sidoName.as("sidoName"),
                                        qSggArea.sggName.as("sggName"),
                                        qEmdArea.emdName.as("emdName")))
                        .from(qEmdArea)
                        .join(qEmdArea.sggArea).on(qSggArea.eq(qEmdArea.sggArea))
                        .join(qSggArea.sidoArea).on(qSidoArea.eq(qSggArea.sidoArea))
                        .orderBy(getOrderSpecifiersByDistance(coordinate))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        if (areaInfos.isEmpty()) {
            return new PageImpl<>(areaInfos, pageable, 0);
        }

        Long totalCount = queryFactory
                .selectDistinct(qEmdArea.count())
                .from(qEmdArea)
                .join(qEmdArea.sggArea).on(qSggArea.eq(qEmdArea.sggArea))
                .join(qSggArea.sidoArea).on(qSidoArea.eq(qSggArea.sidoArea))
                .fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(areaInfos, pageable, totalCount);
    }
}
