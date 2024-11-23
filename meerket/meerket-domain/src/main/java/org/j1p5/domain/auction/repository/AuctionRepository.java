package org.j1p5.domain.auction.repository;

import org.j1p5.domain.auction.entity.AuctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<AuctionEntity,Long> {
}
