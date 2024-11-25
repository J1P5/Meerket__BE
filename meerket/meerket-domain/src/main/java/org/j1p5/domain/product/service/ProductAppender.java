package org.j1p5.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductAppender {
    //productentity save하는 메소드

    private final ProductRepository productRepository;

    public void saveProduct(ProductEntity product){
        productRepository.save(product);
    }
}
