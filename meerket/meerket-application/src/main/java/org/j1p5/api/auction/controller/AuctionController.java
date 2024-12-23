package org.j1p5.api.auction.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.request.PlaceBidRequest;
import org.j1p5.api.auction.dto.request.UpdateBidPriceRequest;
import org.j1p5.api.auction.dto.response.BidHistoryResponse;
import org.j1p5.api.auction.dto.response.PlaceBidResponse;
import org.j1p5.api.auction.dto.response.UpdateBidPriceResponse;
import org.j1p5.api.auction.service.usecase.*;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auctions")
@Tag(name = "auctions", description = "입찰 관련 API")
public class AuctionController {

    private final PlaceBidUseCase placeBidUseCase;
    private final CancelBidUseCase cancelBidUseCase;
    private final UpdateBidPriceUseCase updateBidPriceUseCase;
    private final GetBiddingHistoryUseCase getBiddingHistoryUseCase;
    private final GetCompletedPurchasesUseCase getCompletedPurchasesUseCase;

    @PostMapping("/bid")
    public Response<PlaceBidResponse> bidPlace(
            @LoginUser Long userId, @Validated @RequestBody PlaceBidRequest request) {

        PlaceBidResponse placeBidResponse =
                placeBidUseCase.execute(userId, request.productId(), request.price());
        return Response.onSuccess(placeBidResponse);
    }

    @PatchMapping("/bid")
    public Response<UpdateBidPriceResponse> updateBidPrice(
            @LoginUser Long userId, @Validated @RequestBody UpdateBidPriceRequest request) {
        UpdateBidPriceResponse updateBidPriceResponse =
                updateBidPriceUseCase.execute(
                        request.productId(), userId, request.auctionId(), request.price());

        return Response.onSuccess(updateBidPriceResponse);
    }

    @GetMapping("/bidding")
    public Response<List<BidHistoryResponse>> getBiddingHistory(@LoginUser Long userId) {
        List<BidHistoryResponse> biddingHistoryResponses = getBiddingHistoryUseCase.execute(userId);
        return Response.onSuccess(biddingHistoryResponses);
    }

    @GetMapping("/purchases")
    public Response<List<BidHistoryResponse>> getPurchases(@LoginUser Long userId) {
        List<BidHistoryResponse> purchaseHistoryResponses =
                getCompletedPurchasesUseCase.execute(userId);
        return Response.onSuccess(purchaseHistoryResponses);
    }

    @DeleteMapping("/{auctionId}")
    public Response<Void> cancelBid(@LoginUser Long userId, @PathVariable Long auctionId) {
        cancelBidUseCase.execute(userId, auctionId);
        return Response.onSuccess();
    }
}
