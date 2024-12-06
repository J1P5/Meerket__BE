package org.j1p5.domain.auction.repository;

import org.j1p5.domain.auction.entity.AuctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuctionRepository extends JpaRepository<AuctionEntity, Long> {

    @Query("""
        select a
        from auction a
        join a.product p
        where a.user.id = :userId
        and p.status = org.j1p5.domain.product.entity.ProductStatus.BIDDING
        and a.status = org.j1p5.domain.auction.entity.AuctionStatus.BIDDING
""")
    List<AuctionEntity> findBiddingAuctionsByUserId(@Param("userId") Long userId);


    @Query("""
        select a
        from auction a
        join a.product p
        where a.user.id = :userId
        and p.status = org.j1p5.domain.product.entity.ProductStatus.COMPLETED
        and a.status = org.j1p5.domain.auction.entity.AuctionStatus.AWARDED
""")
    List<AuctionEntity> findCompletedPurchasesByUserId(@Param("userId") Long userId);


}



