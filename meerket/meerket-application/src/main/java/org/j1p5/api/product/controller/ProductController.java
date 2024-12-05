package org.j1p5.api.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.api.product.converter.MultipartFileConverter;
import org.j1p5.api.product.dto.request.ProductCreateRequestDto;
import org.j1p5.api.product.dto.request.ProductUpdateRequest;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService; // 생성자 주입

    /**
     * 중고물품 등록
     *
     * @param request
     * @param images
     * @param userId
     * @return 200, 등록 완료 메세지
     * @author sunghyun0610
     */
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

    /**
     * 사용자 활동지역 거리 기반 100km이내 중고물품 조회
     *
     * @param category
     * @param keyword
     * @param cursor
     * @return 200, 등록 완료 메세지
     * @author sunghyun0610
     */
    @GetMapping
    public Response<CursorResult<ProductResponseInfo>> getProductByAroundPoint(
            @AuthenticationPrincipal UserDetails userDetails,
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

    @GetMapping("/{productId}")
    public Response<ProductResponseDetailInfo> getProductDetails(
            @PathVariable(name = "productId") Long productId, @LoginUser Long userId) {
        return Response.onSuccess(productService.getProductDetail(productId, userId));
    }

    @PatchMapping("/{productId}")
    public Response<Void> updateProduct(
            @PathVariable(name = "productId") Long productId,
            @RequestBody ProductUpdateRequest request,
            @LoginUser Long userId) {
        productService.updateProduct(productId, userId, ProductUpdateRequest.toInfo(request));
        return Response.onSuccess();
    }

    @DeleteMapping("/{productId}")
    public Response<Void> removeProduct(
            @PathVariable(name = "productId") Long productId, @LoginUser Long userId) {

        productService.removeProduct(productId, userId);

        return Response.onSuccess();
    }

    @GetMapping("/categories")
    public Response<CursorResult<ProductResponseInfo>> getProductByCategory(@RequestParam(name = "category") String category,
                                                                            @LoginUser Long userId,@CursorDefault Cursor cursor){

        return Response.onSuccess(productService.getProductsByCategory(userId,cursor,category));
    }

    @GetMapping("/my")
    public Response<CursorResult<MyProductResponseDto>> getMyProducts(@LoginUser Long userId,
                                                                      @CursorDefault Cursor cursor,
                                                                      @RequestParam(name = "status")ProductStatus status
                                                                      ){
        return Response.onSuccess(productService.getMyProducts(userId,cursor,status));
    }

    @GetMapping("/keywords")
    private Response<CursorResult<ProductResponseInfo>> getProductByKeyword(@RequestParam(name = "keyword") String keyword,
                                                                            @LoginUser Long userId,
                                                                            @CursorDefault Cursor cursor){

        return Response.onSuccess(productService.getProductByKeyword(keyword, userId, cursor));

    }
}
