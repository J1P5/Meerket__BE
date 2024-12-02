package org.j1p5.domain.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.j1p5.common.dto.Cursor;
import org.j1p5.common.dto.CursorResult;
import org.j1p5.domain.global.exception.DomainException;
import org.j1p5.domain.image.entitiy.ImageEntity;
import org.j1p5.domain.product.dto.*;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.exception.ProductException;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.user.entity.ActivityArea;
import org.j1p5.domain.user.entity.UserEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.List;

import static org.j1p5.domain.product.exception.ProductException.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {


    private final UserReader userReader;
    private final ProductAppender productAppender;
    private final ImageService imageService;
    private final RegionAuthHandler userRegionauth;
    private final ActivityAreaReader activityAreaReader;
    private final ProductRepository productRepository;
    private final UserLocationNameReader userLocationNameReader;


    @Transactional
    public void registerProduct(Long userId, ProductInfo productInfo, List<File> images) {
        //multipart 자료형은 web에서 처리하고 file만 내려줘라

        UserEntity user = userReader.getUser(userId);//user객체 가져오는 실제 구현부는 UserReader임

//        userRegionauth.checkAuth(user.getId());// 동네 인증된 사용자 체크

        ProductEntity product = ProductInfo.toEntity(productInfo, user);

        //이미지 처리를 위한 로직 -> 그 후 image테이블에 저장

        List<String> imageUrls = imageService.upload(images);
        for (String url : imageUrls) {
            log.info(url);
            ImageEntity image = ImageEntity.from(url);//이미지 엔티티에 저장
            product.addImage(image); // 관계 설정
        }

        productAppender.saveProduct(product);//이떄 연관된 ImageEntity도 같이 저장

    }

    @Transactional
    public CursorResult<ProductResponseInfo> getProducts(Long userId, Cursor cursor) {
        //사용자 활동지역 반경 100km까지 조회
        UserEntity user = userReader.getUser(userId);


        List<ActivityArea> activityAreas = user.getActivityAreas();
        Point coordinate = activityAreaReader.getActivityArea(activityAreas);//이상 사용자 활동지역 좌표


        List<ProductEntity> products = productRepository.findProductsByCursor(coordinate, cursor.cursor(), cursor.size());//거리별 + 커서별 productEntity list조회

        Long nextCursor = products.isEmpty() ? null : products.get(products.size() - 1).getId();//조회된 마지막 물품의 id값 저장

        List<ProductResponseInfo> productResponseInfos = products.stream()
                .map(product -> {
                    MyLocationInfo myLocationInfo = MyLocationInfo.of(userLocationNameReader.getLocationName(activityAreas.get(0)));
                    return ProductResponseInfo.from(product, myLocationInfo);
                })
                .toList();//엔티티 Info Dto로 변환


        return CursorResult.of(productResponseInfos, nextCursor);// Dto ->CursorResult형으로 변환

    }

    @Transactional
    public ProductResponseDetailInfo getProductDetail(Long productId, Long userId){
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(()->new DomainException(PRODUCT_NOT_FOUND));
        UserEntity user = userReader.getUser(userId);

       return ProductResponseDetailInfo.of(product,user);

    }

    @Transactional
    public void updateProduct(Long productId, Long userId, ProductUpdateInfo info){
        UserEntity user = userReader.getUser(userId);
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() ->new DomainException(PRODUCT_NOT_FOUND));

        if(!(product.getUser().equals(user))){
            throw new DomainException(PRODUCT_NOT_AUTHORIZED);
        }

        if(!product.isHasBuyer()){
            product.updateProduct(info);
        }
        else throw new DomainException(PRODUCT_HAS_BUYER);


    }

}
