package org.j1p5.domain.block.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.j1p5.domain.block.entity.BlockEntity;
import org.j1p5.domain.block.entity.QBlockEntity;
import org.j1p5.domain.user.entity.QUserEntity;
import org.j1p5.domain.user.entity.UserEntity;

import java.util.List;

public class BlockRepositoryCustomImpl implements BlockRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public BlockRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QBlockEntity qBlockEntity = QBlockEntity.blockEntity;
    QUserEntity qUserEntity = QUserEntity.userEntity;

    @Override
    public List<BlockEntity> findByUserWithCursor(UserEntity user, Long cursor, Integer size) {
        return jpaQueryFactory
                .selectFrom(qBlockEntity)
                .where(qBlockEntity.id.gt(cursor),
                        qUserEntity.id.eq(user.getId()))
                .join(qUserEntity).on(qBlockEntity.user.eq(qUserEntity))
                .limit(size)
                .fetch();
    }
}
