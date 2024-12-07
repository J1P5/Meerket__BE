package org.j1p5.api.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.api.global.converter.PointConverter;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.api.product.dto.response.CloseEarlyResponseDto;
import org.j1p5.api.product.dto.response.CreateProductResponseDto;
import org.j1p5.api.product.dto.response.MyProductResponseDto;
import org.j1p5.common.dto.Cursor;
import org.j1p5.common.dto.CursorResult;
import org.j1p5.domain.activityArea.entity.ActivityArea;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.j1p5.domain.global.exception.DomainException;
import org.j1p5.domain.image.entitiy.ImageEntity;
import org.j1p5.domain.product.dto.*;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.entity.ProductStatus;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.product.service.ImageService;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.j1p5.api.auction.exception.AuctionException.BID_NOT_FOUND;
import static org.j1p5.api.product.exception.ProductException.*;
import static org.j1p5.domain.global.exception.DomainErrorCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductUserReader userReader;
    private final ProductAppender productAppender;
    private final ImageService imageService;
    private final RegionAuthHandler userRegionauth;
    private final ActivityAreaReader activityAreaReader;
    private final ProductRepository productRepository;
    private final UserLocationNameReader userLocationNameReader;
    private final UserRepository userRepository;
    private final FcmService fcmService;
    private final AuctionRepository auctionRepository;


    @Transactional
    public CreateProductResponseDto registerProduct(Long userId, ProductInfo productInfo, List<File> images) {
        // multipart 자료형은 web에서 처리하고 file만 내려줘라

        UserEntity user = userReader.getUser(userId); // user객체 가져오는 실제 구현부는 UserReader임

        //        userRegionauth.checkAuth(user.getId());// 동네 인증된 사용자 체크

        ProductEntity product = ProductInfo.toEntity(productInfo, user);

        // 이미지 처리를 위한 로직 -> 그 후 image테이블에 저장

        List<String> imageUrls = imageService.upload(images);
        for (String url : imageUrls) {
            log.info(url);
            ImageEntity image = ImageEntity.from(url); // 이미지 엔티티에 저장
            product.addImage(image); // 관계 설정
        }
        product.createThumbnail(imageUrls.get(0));

        productAppender.saveProduct(product); // 이떄 연관된 ImageEntity도 같이 저장

        return CreateProductResponseDto.from(product);
    }

    @Transactional
    public CursorResult<ProductResponseInfo> getProducts(Long userId, Cursor cursor) {
        // 사용자 활동지역 반경 100km까지 조회
        UserEntity user = userReader.getUser(userId);

        List<ActivityArea> activityAreas = user.getActivityAreas();
        Point coordinate = activityAreaReader.getActivityArea(activityAreas); // 이상 사용자 활동지역 좌표

        List<ProductEntity> products = productRepository.findProductsByCursor(coordinate, cursor.cursor(), cursor.size()); // 거리별 + 커서별 productEntity list조회

        Long nextCursor = products.isEmpty() ? null : products.get(products.size() - 1).getId(); // 조회된 마지막 물품의 id값 저장

        List<ProductResponseInfo> productResponseInfos = products.stream()
                .map(product -> {
                    MyLocationInfo myLocationInfo = MyLocationInfo.of(userLocationNameReader.getLocationName(activityAreas.get(0)));
                    return ProductResponseInfo.from(product, myLocationInfo);
                })
                .toList(); // 엔티티 Info Dto로 변환

        return CursorResult.of(productResponseInfos, nextCursor); // Dto ->CursorResult형으로 변환
    }

    @Transactional
    public ProductResponseDetailInfo getProductDetail(Long productId, Long userId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(PRODUCT_NOT_FOUND));
        UserEntity user = userReader.getUser(userId);
        if (product.getStatus().equals(ProductStatus.DELETED)) {
            throw new DomainException(PRODUCT_IS_DELETED);
        }
        log.info("");
        AuctionEntity winningAuction = auctionRepository.findHighestBidder(productId)
                .orElseThrow(() -> new DomainException(BID_NOT_FOUND));
        log.info("최고가 "+ winningAuction.getPrice());
        //userId로 각 구매자에 따른 auctionEntity조회 해야함.
        AuctionEntity myAuction = auctionRepository.findAuctionByUserIdAndProductId(productId,userId);
        log.info("내 입찰가 "+myAuction.getPrice());
        return ProductResponseDetailInfo.of(product, user, winningAuction,myAuction);
    }

    @Transactional
    public void updateProduct(Long productId, Long userId, ProductUpdateInfo info) {
        UserEntity user = userReader.getUser(userId);
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(PRODUCT_NOT_FOUND));

        if (!(product.getUser().equals(user))) {
            throw new DomainException(PRODUCT_NOT_AUTHORIZED);
        }

        Point coordinate = PointConverter.createPoint(info.longtitude(), info.latitude());

        if (!product.isHasBuyer()) {
            product.updateProduct(info, coordinate);
        } else throw new DomainException(PRODUCT_HAS_BUYER);
    }

    @Transactional
    public void removeProduct(Long productId, Long userId) {
        UserEntity user = userReader.getUser(userId);

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(PRODUCT_NOT_FOUND));
        if (!product.getUser().equals(user)) {
            throw new DomainException(PRODUCT_NOT_AUTHORIZED);
        }
        product.updateStatusToDelete(product);
        fcmService.sendBuyerProductDeleted(productId);
    }

    @Transactional
    public CursorResult<ProductResponseInfo> getProductsByCategory(Long userId, Cursor cursor, String category) {
        if (category.isEmpty() || category == null) throw new DomainException(INVALID_PRODUCT_CATEGORY);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(USER_NOT_FOUND));
        Point userCoordinate = activityAreaReader.getActivityArea(user.getActivityAreas());

        List<ProductEntity> productEntityList = productRepository.findProductByCategory(userCoordinate, category, cursor.cursor(), cursor.size());

        Long nextCursor = productEntityList.isEmpty() ? null : productEntityList.get(productEntityList.size() - 1).getId();

        List<ProductResponseInfo> productResponseInfos = productEntityList.stream()
                .map(product -> {
                    MyLocationInfo myLocationInfo = MyLocationInfo.of(userLocationNameReader.getLocationName(user.getActivityAreas().get(0)));
                    return ProductResponseInfo.from(product, myLocationInfo);
                })
                .toList();
        return CursorResult.of(productResponseInfos, nextCursor);
    }

    @Transactional
    public CursorResult<MyProductResponseDto> getMyProducts(Long userId, Cursor cursor, ProductStatus status) {
        List<ProductEntity> productEntityList = productRepository.findProductByUserId(userId, cursor.cursor(), cursor.size(), status);

        Long nextCursor = productEntityList.isEmpty() ? null : productEntityList.get(productEntityList.size() - 1).getId();

        List<MyProductResponseDto> myProductResponseDtos = productEntityList.stream()
                .map(MyProductResponseDto::from)
                .toList();

        return CursorResult.of(myProductResponseDtos, nextCursor);

    }

    @Transactional
    public CursorResult<ProductResponseInfo> getProductByKeyword(String keyword, Long userId, Cursor cursor) {
        if (keyword.isEmpty() || keyword == null) {
            throw new DomainException(INVALID_PRODUCT_KEYWORD);
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(USER_NOT_FOUND));
        Point userCoordinate = activityAreaReader.getActivityArea(user.getActivityAreas());

        List<ProductEntity> productEntityList = productRepository.findProductByKeyword(userCoordinate, keyword, cursor.cursor(), cursor.size());
        Long nextCursor = productEntityList.isEmpty() ? null : productEntityList.get(productEntityList.size() - 1).getId();

        List<ProductResponseInfo> productResponseInfos = productEntityList.stream()
                .map(product -> {
                    MyLocationInfo myLocationInfo = MyLocationInfo.of(userLocationNameReader.getLocationName(user.getActivityAreas().get(0)));
                    return ProductResponseInfo.from(product, myLocationInfo);
                })
                .toList();
        return CursorResult.of(productResponseInfos, nextCursor);
    }

    public CloseEarlyResponseDto closeProductEarly(Long productId, Long userId) {
        UserEntity user = userReader.getUser(userId);

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(PRODUCT_NOT_FOUND));
        if (!product.getUser().equals(user)) {
            throw new DomainException(PRODUCT_NOT_AUTHORIZED);
        }//판매자인지 확인
        Duration remainingTime = Duration.between(LocalDateTime.now(), product.getExpiredTime());
        if(remainingTime.toHours()<2){
            throw new DomainException(INVALID_PRODUCT_EARLY_CLOSED);
        }
        if(!product.isHasBuyer()){
            throw new DomainException(PRODUCT_HAS_NO_BUYER);
        }
        product.updateIsEarly();
        product.updateExpiredTime();

        //조기마감후 입찰은 내가 설정한 각겨보다 상향수정만 가능
        //fcm을 통한 알림 구현로직 추가 예정
        fcmService.sendBuyerCloseEarlyMessage(productId);

        return CloseEarlyResponseDto.of(productId);

    }


    public void updateWinningPrice(Long productId, int winningPrice) {

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new WebException(PRODUCT_NOT_FOUND));

        productEntity.updateWinningPrice(winningPrice);
    }



}
