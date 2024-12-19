package org.j1p5.domain.auction.repository;

import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<AuctionEntity, Long> {

    @Query("""
                select a
                from auction a
                join a.product p
                where a.isDelete = false
                and a.user.id = :userId
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


    @Query("""
                    select case when count(a) > 0 then true else false end
                    from auction a
                    where a.user.id = :userId
                    and a.product.id = :productId
                    and a.status = org.j1p5.domain.auction.entity.AuctionStatus.BIDDING
            
            """)
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);


    @Query("""
                    select a
                    from auction a
                    where a.product.id = :productId
                    and a.status = org.j1p5.domain.auction.entity.AuctionStatus.BIDDING
                    order by a.price desc , a.updatedAt asc 
                    limit 1
            """
    )
    Optional<AuctionEntity> findHighestBidder(@Param("productId") Long productId);




    @Query("""
                    select a
                    from auction a
                    where a.product.id = :productId
                    and a.status = org.j1p5.domain.auction.entity.AuctionStatus.BIDDING
            
            """)
    List<AuctionEntity> findAuctionEntitiesByProductId(@Param("productId") Long productId);

    @Query("""
    select a
    from auction a
    join a.product p
    where p.id = :productId
    and a.user.id = :userId
    and a.status = org.j1p5.domain.auction.entity.AuctionStatus.BIDDING
""")
    Optional<AuctionEntity> findAuctionByUserIdAndProductId(
            @Param("productId") Long productId,
            @Param("userId") Long userId
    );

    List<AuctionEntity> findByUser(UserEntity user);


}





