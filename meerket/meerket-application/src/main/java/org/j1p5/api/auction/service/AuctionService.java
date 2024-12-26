package org.j1p5.api.auction.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.BidHistoryResponse;
import org.j1p5.api.auction.exception.AuctionException;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.api.product.exception.ProductException;
import org.j1p5.api.product.service.EmdNameReader;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.entity.AuctionStatus;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    // 해당 입찰이 실제 해당 유저의 입찰인지 확인
    public void verifyUserBidOwnership(Long userId, Long auctionId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new WebException(AuctionException.BID_USER_NOT_FOUND));

        AuctionEntity auctionEntity = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new WebException(AuctionException.BID_NOT_FOUND));

        if (!Objects.equals(userEntity.getId(), auctionEntity.getUser().getId())) {
            throw new WebException(AuctionException.BID_USER_NOT_AUTHORIZED);
        }
    }

    // 현재 조기마감 상태인지 확인
    public boolean isEarlyClosure(Long productId) {
        ProductEntity productEntity = getProductEntity(productId);

        return productEntity.isEarly();
    }


    // 입찰 내역 Response 생성
    public List<BidHistoryResponse> getAuctionHistoryResponses(List<AuctionEntity> auctionEntities) {
        return auctionEntities.stream()
                .map(auctionEntity -> {
                    ProductEntity productEntity = getProductEntity(auctionEntity.getProduct().getId());
                    return new BidHistoryResponse(
                            productEntity.getId(),
                            auctionEntity.getId(),
                            productEntity.getTitle(),
                            productEntity.getThumbnail(),
                            auctionEntity.getPrice(),
                            EmdNameReader.getEmdName(auctionEntity.getUser()),
                            productEntity.getCreatedAt(),
                            productEntity.getMinPrice(),
                            productEntity.getExpiredTime()
                    );
                })
                .toList();
    }

    // 최고 입찰자 찾기
    public AuctionEntity findByHighestBidder(Long productId) {
        AuctionEntity auctionEntity = auctionRepository.findHighestBidder(productId)
                .orElseThrow(() -> new WebException(AuctionException.BID_USER_NOT_FOUND));

        return auctionEntity;
    }

    // 거래완료 시 Auction의 status변경
    public void updateAuctionStatusToAwarded(Long productId) {
        AuctionEntity auctionEntity = findByHighestBidder(productId);
        auctionEntity.updateStatus(AuctionStatus.AWARDED);
    }

    private ProductEntity getProductEntity(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new WebException(ProductException.PRODUCT_NOT_FOUND));
    }

    @Transactional
    public void withdraw(UserEntity user) {
        List<AuctionEntity> auctions = auctionRepository.findByUser(user);

        if (auctions.isEmpty()) {
            return;
        }

        auctions.forEach(AuctionEntity::withdraw);
    }

}
