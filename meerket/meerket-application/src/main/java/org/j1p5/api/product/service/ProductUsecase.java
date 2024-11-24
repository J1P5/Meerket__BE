package org.j1p5.api.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.product.dto.request.ProductRequestDto;
import org.j1p5.common.exception.CustomException;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.service.UserReader;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.j1p5.common.exception.GlobalErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductUsecase {

    private final UserReader userReader;
    private final ImageService imageService;


    @Transactional
    public void registerProduct(String email, ProductRequestDto productRequestDto, List<MultipartFile> images) {

        UserEntity user = userReader.getUser(email);//user객체 가져오는 실제 구현부는 UserReader임

        ProductEntity product = ProductRequestDto.toEntity(productRequestDto, user);

        //이미지 처리를 위한 로직 -> 그 후 image테이블에 저장


    }

}
