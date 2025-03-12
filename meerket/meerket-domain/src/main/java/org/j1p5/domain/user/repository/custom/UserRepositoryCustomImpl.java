package org.j1p5.domain.user.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.j1p5.domain.activityArea.entity.QActivityArea;
import org.j1p5.domain.block.BlockUserDto;
import org.j1p5.domain.block.entity.QBlockEntity;
import org.j1p5.domain.user.entity.QEmdArea;
import org.j1p5.domain.user.entity.QUserEntity;

import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public UserRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QBlockEntity qBlockEntity = QBlockEntity.blockEntity;
    QUserEntity qUserEntity = QUserEntity.userEntity;
    QEmdArea qEmdArea = QEmdArea.emdArea;
    QActivityArea qActivityArea = QActivityArea.activityArea;

    @Override
    public List<BlockUserDto> findBlockUserByIds(List<Long> userIds) {
        List<BlockUserDto> blockUsers =
                jpaQueryFactory
                        .selectDistinct(
                                Projections.constructor(
                                        BlockUserDto.class,
                                        qBlockEntity.id.as("id"),
                                        qUserEntity.id.as("userId"),
                                        qUserEntity.nickname.as("nickname"),
                                        qUserEntity.imageUrl.as("imageUrl"),
                                        qEmdArea.emdName.as("emdName")
                                )
                        )
                        .from(qUserEntity)
                        //TODO : 역정규화를 사용한 리팩토링(성능개선) 필요
                        .leftJoin(qBlockEntity).on(qBlockEntity.user.eq(qUserEntity))
                        .leftJoin(qActivityArea).on(qActivityArea.user.eq(qUserEntity))
                        .leftJoin(qEmdArea).on(qActivityArea.emdArea.eq(qEmdArea))
                        .where(qUserEntity.id.in(userIds))
                        .fetch();

        return blockUsers;
    }
}
