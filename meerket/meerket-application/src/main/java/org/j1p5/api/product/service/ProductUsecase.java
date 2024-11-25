package org.j1p5.api.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.product.dto.request.ProductRequestDto;
import org.j1p5.api.product.dto.response.ProductResponseDto;
import org.j1p5.domain.image.entitiy.ImageEntity;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.product.service.ProductAppender;
import org.j1p5.domain.product.service.UserReader;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductUsecase {

    private final UserReader userReader;
    private final ProductAppender productAppender;
    private final ImageService imageService;


    @Transactional
    public void registerProduct(String email, ProductRequestDto productRequestDto, List<MultipartFile> images) {

        UserEntity user = userReader.getUser(email);//user객체 가져오는 실제 구현부는 UserReader임

        ProductEntity product = ProductRequestDto.toEntity(productRequestDto, user);

        //이미지 처리를 위한 로직 -> 그 후 image테이블에 저장

        List<String> imageUrls = imageService.upload(images);
        for (String url : imageUrls) {
            ImageEntity image = ImageEntity.from(url);//이미지 엔티티에 저장
            product.addImage(image); // 관계 설정
        }
        
        productAppender.saveProduct(product);//이떄 연관된 ImageEntity도 같이 저장

    }

    @Transactional
    public ProductResponseDto selectAllProduct(String category, String keyword){
        //사용자 활동지역 반경 100km까지 조회
    }

}
