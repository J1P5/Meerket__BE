package org.j1p5.api.auction.service.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.response.PlaceBidResponse;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.domain.global.exception.DomainException;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import static org.j1p5.api.product.exception.ProductException.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlaceBidUseCase {

    private final AuctionService auctionService;
    private final FcmService fcmService;
    private final ProductRepository productRepository;

    public PlaceBidResponse execute(Long userId, Long productId, int price) {

        auctionService.checkDuplicateBid(userId, productId);

        PlaceBidResponse placeBidResponse = auctionService.placeBid(userId, productId, price);

        ProductEntity product = productRepository.findById(productId)
                        .orElseThrow(() -> new DomainException(PRODUCT_NOT_FOUND));
        product.updateHasBuyer();

        fcmService.sendSellerBidMessage(productId);

        return placeBidResponse;
    }

}
