package org.j1p5.api.auction.fixture;

import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.user.entity.UserEntity;

import java.lang.reflect.Field;

public class AuctionFixture {

    public static AuctionEntity withUserId(Long userId) {
        UserEntity user = UserFixture.withId(userId);
        ProductEntity product = ProductFixture.withNo();

        AuctionEntity auction = AuctionEntity.create(user, product, 1000);
        try {
            Field field = AuctionEntity.class.getDeclaredField("user");
            field.setAccessible(true);
            field.set(auction, user);
        } catch (Exception e) {
            throw new RuntimeException("AuctionFixture 생성 실패", e);
        }

        return auction;
    }

    public static AuctionEntity withProductAndAuctionId(ProductEntity product, Long auctionId) {
        UserEntity user = UserFixture.withNo();

        AuctionEntity auction = AuctionEntity.create(user, product, 1000);
        try {
            Field field = AuctionEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(auction, auctionId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return auction;
    }


    public static AuctionEntity withPrice(int price) {
        UserEntity user = UserFixture.withNo();
        ProductEntity product = ProductFixture.withNo();

        AuctionEntity auction = AuctionEntity.create(user, product, price);
        return auction;
    }






}
