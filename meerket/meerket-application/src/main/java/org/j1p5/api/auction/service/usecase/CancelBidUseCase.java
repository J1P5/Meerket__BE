package org.j1p5.api.auction.service.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.j1p5.domain.global.exception.DomainException;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.infrastructure.global.exception.InfraException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.j1p5.api.product.exception.ProductException.PRODUCT_NOT_FOUND;
import static org.j1p5.infrastructure.fcm.exception.FcmException.CANCEL_BID_FCM_SELLER_ERROR;

@Service
@RequiredArgsConstructor
public class CancelBidUseCase {

    private final AuctionService auctionService;
    private final FcmService fcmService;
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void execute(Long userId, Long auctionId) {

        auctionService.verifyUserBidOwnership(userId, auctionId);

        Long productId = auctionService.cancelBid(auctionId);

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(PRODUCT_NOT_FOUND));

        List<AuctionEntity> remainAuctionEntities = auctionRepository.findAuctionEntitiesByProductId(productId);
        if(remainAuctionEntities.isEmpty()){
            product.updateHasBuyerFalse();
        }

        try {
            fcmService.sendSellerBidCancelMessage(productId);
        } catch (Exception e) {
            throw new InfraException(CANCEL_BID_FCM_SELLER_ERROR);
        }
    }
}
