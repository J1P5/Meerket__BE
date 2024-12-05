package org.j1p5.api.auction.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.PlaceBidResponse;
import org.j1p5.api.auction.exception.AuctionException;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.exception.ProductException;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    // 입찰하기
    public PlaceBidResponse placeBid(Long userId, Long productId, int price) {

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new WebException(ProductException.PRODUCT_NOT_FOUND));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new WebException(AuctionException.BID_USER_NOT_FOUND));

        if (productEntity.getMinPrice() > price) {
            throw new WebException(AuctionException.AUCTION_MIN_PRICE_ERROR);
        }

        AuctionEntity auctionEntity = AuctionEntity.create(userEntity, productEntity, price);

        auctionRepository.save(auctionEntity);

        return PlaceBidResponse.fromEntity(auctionEntity);
    }


    public void verifyUserBidOwnership(Long userId, Long auctionId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new WebException(AuctionException.BID_USER_NOT_FOUND));

        AuctionEntity auctionEntity = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new WebException(AuctionException.BID_NOT_FOUND));

        if (userEntity.getId() != auctionEntity.getUser().getId()) {
            throw new WebException(AuctionException.BID_USER_NOT_AUTHORIZED);
        }
    }



    // 현재 조기마감 상태인지 확인
    public boolean isEarlyClosure(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new WebException(ProductException.PRODUCT_NOT_FOUND));

        return productEntity.isEarly();
    }


    // 조기마감 상태일때의 수정
    public AuctionEntity updateBidPriceForEarlyClosure(Long auctionId, int price) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new WebException(AuctionException.BID_NOT_FOUND));

        if (auctionEntity.getPrice() >= price) {
            throw new WebException(AuctionException.BID_AMOUNT_TOO_LOW);
        }

        auctionEntity.updatePrice(price);
        auctionRepository.save(auctionEntity);
        return auctionEntity;
    }


    // 조기마감 상태가 아닐때의 수정
    public AuctionEntity updateBidPriceWithMinimumLimit(Long auctionId, Long productId, int price) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new WebException(ProductException.PRODUCT_NOT_FOUND));

        if (productEntity.getMinPrice() > price) {
            throw new WebException(AuctionException.AUCTION_MIN_PRICE_ERROR);
        }

        AuctionEntity auctionEntity = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new WebException(AuctionException.BID_NOT_FOUND));

        auctionEntity.updatePrice(price);
        auctionRepository.save(auctionEntity);
        return auctionEntity;
    }


}
