package org.j1p5.domain.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.j1p5.domain.image.entitiy.ImageEntity;
import org.j1p5.domain.product.dto.ProductInfo;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserReader userReader;
    private final ProductAppender productAppender;
    private final ImageService imageService;
    private final UserRegionauth userRegionauth;


    @Transactional
    public void registerProduct(String email, ProductInfo productInfo, List<File> images) {
        //multipart 자료형은 web에서 처리하고 file만 내려줘라

        UserEntity user = userReader.getUser(email);//user객체 가져오는 실제 구현부는 UserReader임

        userRegionauth.checkAuth(user.getId());// 동네 인증된 사용자 체크

        ProductEntity product = ProductInfo.toEntity(productInfo, user);

        //이미지 처리를 위한 로직 -> 그 후 image테이블에 저장

        List<String> imageUrls = imageService.upload(images);
        for (String url : imageUrls) {
            ImageEntity image = ImageEntity.from(url);//이미지 엔티티에 저장
            product.addImage(image); // 관계 설정
        }
        
        productAppender.saveProduct(product);//이떄 연관된 ImageEntity도 같이 저장

    }


}
