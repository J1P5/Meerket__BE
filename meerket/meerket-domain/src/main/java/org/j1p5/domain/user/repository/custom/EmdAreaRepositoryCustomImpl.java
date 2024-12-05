package org.j1p5.domain.areaAuth.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.j1p5.domain.auth.area.entity.QAreaAuthEntity;
import org.j1p5.domain.user.entity.EmdArea;

public class AreaAuthRepositoryCustomImpl implements AreaAuthRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AreaAuthRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QAreaAuthEntity qAreaAuthEntity = QAreaAuthEntity.areaAuthEntity;

    @Override
    public boolean existsInRequestArea(Double longitude, Double latitude, EmdArea emdArea) {
        boolean areaInfos =
                queryFactory.selectFrom(emdArea)
                        .where(coordinateCondition(areaAuthInfo.longitude(), areaAuthInfo.latitude(), areaAuthInfo.emdId()))
                        .fetchOne());

        return areaInfos;
    }
}
