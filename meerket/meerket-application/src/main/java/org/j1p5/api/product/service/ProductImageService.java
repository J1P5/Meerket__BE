package org.j1p5.api.product.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.j1p5.domain.image.entitiy.ImageEntity;
import org.j1p5.domain.image.repository.ImageRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void withdraw(ProductEntity product) {
        List<ImageEntity> images = imageRepository.findByProduct(product);

        if (images.isEmpty()) {
            return;
        }

        images.forEach(ImageEntity::withdraw);
    }
}
