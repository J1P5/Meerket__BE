package org.j1p5.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.api.product.converter.MultipartFileConverter;
import org.j1p5.api.product.dto.request.ProductCreateRequestDto;
import org.j1p5.api.product.dto.request.ProductUpdateRequest;
import org.j1p5.api.product.dto.response.CloseEarlyResponseDto;
import org.j1p5.api.product.dto.response.CreateProductResponseDto;
import org.j1p5.api.product.dto.response.MyProductResponseDto;
import org.j1p5.api.product.service.ProductService;
import org.j1p5.common.annotation.CursorDefault;
import org.j1p5.common.dto.Cursor;
import org.j1p5.common.dto.CursorResult;
import org.j1p5.domain.product.dto.ProductInfo;
import org.j1p5.domain.product.dto.ProductResponseDetailInfo;
import org.j1p5.domain.product.dto.ProductResponseInfo;
import org.j1p5.domain.product.entity.ProductStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "products", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService; // 생성자 주입

    @Operation(summary = "중고물품 등록", description = "중고물품을 등록합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "물품 등록 성공"),
                    @ApiResponse(responseCode = "400", description = "빈 파일, 파일 확장자 오류"),
                    @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 에러, 이미지 변환 에러, 이미지 s3 등록 에러")
            }
    )
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<CreateProductResponseDto> createProduct(
            @RequestPart(name = "request") ProductCreateRequestDto request,
            @RequestPart(name = "images", required = false) List<MultipartFile> images,
            @LoginUser Long userId) {

        // 세션이들어옴 -> 세션에있는 userid뽑아냄 ->이거 이용해서 지역인증 테이블에서 findById , 추후에 user role추가

        ProductInfo productInfo = ProductCreateRequestDto.toInfo(request);
        List<File> imageFiles = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            imageFiles = MultipartFileConverter.convertMultipartFilesToFiles(images);
        }

        CreateProductResponseDto responseDto = productService.registerProduct(userId, productInfo, imageFiles);

        return Response.onSuccess(responseDto);
    }


    @Operation(summary = "중고물품 조회", description = "중고물품을 조회합니다. 키워드와 카테고리는 선택사항입니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "물품 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 에러")
            }
    )
    @GetMapping
    public Response<CursorResult<ProductResponseInfo>> getProductByAroundPoint(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "keyword", required = false) String keyword,
            @CursorDefault Cursor cursor,
            @LoginUser Long userId) {

        // 서비스 호출
        CursorResult<ProductResponseInfo> products =
                productService.getProducts(
                        userId, cursor); // cursorResult형 조회된 productResponseInfo 반환

        return Response.onSuccess(products);
    }


    @Operation(summary = "마감된 중고물품 조회", description = "마감된 중고물품을 조회합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "물품 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 에러")
            }
    )
    @GetMapping("/completed")
    public Response<CursorResult<ProductResponseInfo>> getCompletedProducts(
            @CursorDefault Cursor cursor,
            @LoginUser Long userId
    ) {

        CursorResult<ProductResponseInfo> completedProducts = productService.getCompletedProducts(userId, cursor);

        return Response.onSuccess(completedProducts);
    }


    @Operation(summary = "중고물품 상세 조회", description = "특정 중고물품을 조회합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "물품 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "물품 또는 유저가 존재하지 않습니다."),
                    @ApiResponse(responseCode = "410", description = "삭제된 게시물입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 에러")
            }
    )
    @GetMapping("/{productId}")
    public Response<ProductResponseDetailInfo> getProductDetails(
            @PathVariable(name = "productId") Long productId, @LoginUser Long userId) {

        return Response.onSuccess(productService.getProductDetail(productId, userId));
    }


    @Operation(summary = "중고물품 수정", description = "특정 중고물품을 수정합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "물품 수정 성공"),
                    @ApiResponse(responseCode = "403", description = "상품 수정 권한이 없습니다."),
                    @ApiResponse(responseCode = "404", description = "물품 또는 유저가 존재하지 않습니다."),
                    @ApiResponse(responseCode = "405", description = "입찰자가 있는 상품은 수정할 수 없습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 에러")
            }
    )
    @PatchMapping("/{productId}")
    public Response<Void> updateProduct(
            @PathVariable(name = "productId") Long productId,
            @RequestBody ProductUpdateRequest request,
            @LoginUser Long userId) {
        productService.updateProduct(productId, userId, ProductUpdateRequest.toInfo(request));
        return Response.onSuccess();
    }


    @Operation(summary = "중고물품 삭제", description = "특정 중고물품을 삭제합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "물품 삭제 성공"),
                    @ApiResponse(responseCode = "403", description = "상품 삭제 권한이 없습니다."),
                    @ApiResponse(responseCode = "404", description = "물품 또는 유저가 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 에러")
            }
    )
    @DeleteMapping("/{productId}")
    public Response<Void> removeProduct(
            @PathVariable(name = "productId") Long productId, @LoginUser Long userId) {

        productService.removeProduct(productId, userId);

        return Response.onSuccess();
    }


    @Operation(summary = "중고물품 카테고리 기반 조회", description = "카테고리 별 물품을 조회합니다.")
    @GetMapping("/categories")
    public Response<CursorResult<ProductResponseInfo>> getProductByCategory(@RequestParam(name = "category") String category,
                                                                            @LoginUser Long userId, @CursorDefault Cursor cursor) {

        return Response.onSuccess(productService.getProductsByCategory(userId, cursor, category));
    }


    @Operation(summary = "판매내역 조회", description = "나의 (입찰, 거래중, 거래완료, 삭제) 내역을 조회합니다.")
    @GetMapping("/my")
    public Response<CursorResult<MyProductResponseDto>> getMyProducts(@LoginUser Long userId,
                                                                      @CursorDefault Cursor cursor,
                                                                      @RequestParam(name = "status") ProductStatus status
    ) {
        return Response.onSuccess(productService.getMyProducts(userId, cursor, status));
    }

    @Operation(summary = "중고물품 키워드 기반 조회", description = "검색어로 물품을 조회합니다.")
    @GetMapping("/keywords")
    private Response<CursorResult<ProductResponseInfo>> getProductByKeyword(@RequestParam(name = "keyword") String keyword,
                                                                            @LoginUser Long userId,
                                                                            @CursorDefault Cursor cursor) {

        return Response.onSuccess(productService.getProductByKeyword(keyword, userId, cursor));

    }

    @Operation(summary = "조기마감하기", description = "판매자가 특정 중고물품을 조기마감합니다.")
    @PostMapping("/{productId}/early-close")
    public Response<CloseEarlyResponseDto> closeEarly(@PathVariable(name = "productId") Long productId,
                                                      @LoginUser Long userId) {
        return Response.onSuccess(productService.closeProductEarly(productId, userId));
    }


    @Operation(summary = "거래 완료", description = "판매자가 구매자와 거래를 완료합니다.")
    @PatchMapping("/{productId}/complete")
    public Response<Void> markProductAsCompleted(
            @PathVariable Long productId,
            @LoginUser Long userId
    ) {

        productService.markProductAsCompleted(productId, userId);
        return Response.onSuccess();
    }






}
