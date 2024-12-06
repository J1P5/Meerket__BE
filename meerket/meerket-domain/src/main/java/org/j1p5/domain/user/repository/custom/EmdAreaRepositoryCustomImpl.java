package org.j1p5.domain.user.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.j1p5.domain.user.entity.EmdArea;
import org.j1p5.domain.user.entity.QEmdArea;
import org.locationtech.jts.geom.Point;

public class EmdAreaRepositoryCustomImpl implements EmdAreaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public EmdAreaRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QEmdArea qEmdArea = QEmdArea.emdArea;

    private BooleanBuilder isPointInPolygonCondition(Point coordinate, EmdArea emdArea) {
        BooleanBuilder builder = new BooleanBuilder();

        // WKT(Point) 문자열 생성
        String wktPoint = String.format("POINT(%s %s)", coordinate.getY(), coordinate.getX());

        BooleanExpression stContainsCondition = Expressions.booleanTemplate(
                "ST_Contains({0}, ST_GeomFromText({1}, 4326))",
                emdArea.getGeom(),
                wktPoint
        );

        builder.and(qEmdArea.id.eq(emdArea.getId()));
        builder.and(stContainsCondition);
        return builder;
    }

    @Override
    public boolean existsInRequestArea(Point coordinate, EmdArea emdArea) {
        return queryFactory.selectFrom(qEmdArea)
                        .where(isPointInPolygonCondition(coordinate, emdArea))
                        .fetchFirst() != null;
    }
}
