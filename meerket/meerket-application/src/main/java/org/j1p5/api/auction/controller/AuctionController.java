package org.j1p5.api.auction.controller;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auction.dto.request.PlaceBidRequest;
import org.j1p5.api.auction.dto.request.UpdateBidPriceRequest;
import org.j1p5.api.auction.dto.response.PlaceBidResponse;
import org.j1p5.api.auction.dto.response.UpdateBidPriceResponse;
import org.j1p5.api.auction.service.usecase.CancelBidUseCase;
import org.j1p5.api.auction.service.usecase.PlaceBidUseCase;
import org.j1p5.api.auction.service.usecase.UpdateBidPriceUseCase;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auctions")
public class AuctionController {

    private final PlaceBidUseCase placeBidUseCase;
    private final CancelBidUseCase cancelBidUseCase;
    private UpdateBidPriceUseCase updateBidPriceUseCase;


    @PostMapping("/bid")
    public Response<PlaceBidResponse> bidPlace(
            @LoginUser Long userId,
            @Validated @RequestBody PlaceBidRequest request
            ) {

        PlaceBidResponse placeBidResponse = placeBidUseCase.execute(userId, request.productId(), request.price());
        return Response.onSuccess(placeBidResponse);
    }


    @PatchMapping("/bid")
    public Response<UpdateBidPriceResponse> updateBidPrice(
            @LoginUser Long userId,
            @Validated @RequestBody UpdateBidPriceRequest request
    ) {
        UpdateBidPriceResponse updateBidPriceResponse = updateBidPriceUseCase
                .execute(request.productId(), userId, request.auctionId(), request.price());

        return Response.onSuccess(updateBidPriceResponse);
    }





}
