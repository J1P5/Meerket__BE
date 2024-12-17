package org.j1p5.domain.block.repository;

import org.j1p5.domain.block.entity.BlockEntity;
import org.j1p5.domain.block.repository.querydsl.BlockRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<BlockEntity, Long>, BlockRepositoryCustom {}
