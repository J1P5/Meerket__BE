package org.j1p5.domain.user.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.j1p5.domain.activityArea.entity.QActivityArea;
import org.j1p5.domain.block.BlockUserInfo;
import org.j1p5.domain.user.entity.QEmdArea;
import org.j1p5.domain.user.entity.QUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public UserRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QUserEntity qUserEntity = QUserEntity.userEntity;
    QEmdArea qEmdArea = QEmdArea.emdArea;
    QActivityArea qActivityArea = QActivityArea.activityArea;

    @Override
    public Page<BlockUserInfo> findBlockUserByIds(List<Long> userIds, Pageable pageable) {
        List<BlockUserInfo> blockUserInfos =
                jpaQueryFactory
                        .selectDistinct(
                                Projections.constructor(
                                        BlockUserInfo.class,
                                        qUserEntity.id.as("userId"),
                                        qUserEntity.nickname.as("nickname"),
                                        qUserEntity.imageUrl.as("imageUrl"),
                                        qEmdArea.emdName.as("emdName")
                                )
                        )
                        .from(qUserEntity)
                        //TODO : 역정규화를 사용한 리팩토링(성능개선) 필요
                        .join(qActivityArea).on(qActivityArea.user.eq(qUserEntity))
                        .join(qEmdArea).on(qActivityArea.emdArea.eq(qEmdArea))
                        .where(qUserEntity.id.in(userIds))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        if (blockUserInfos.isEmpty()) {
            return new PageImpl<>(blockUserInfos, pageable, 0);
        }

        Long totalCount = jpaQueryFactory
                .selectDistinct(qUserEntity.count())
                .from(qUserEntity)
                .join(qActivityArea).on(qActivityArea.user.eq(qUserEntity))
                .join(qEmdArea).on(qActivityArea.emdArea.eq(qEmdArea))
                .where(qUserEntity.id.in(userIds))
                .fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(blockUserInfos, pageable, totalCount);
    }
}
