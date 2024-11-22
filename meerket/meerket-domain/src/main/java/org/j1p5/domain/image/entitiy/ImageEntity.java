package org.j1p5.domain.image.entitiy;

import jakarta.persistence.*;
import org.j1p5.domain.global.entity.BaseEntity;
import org.j1p5.domain.product.entity.ProductEntity;


@Entity
public class ImageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;



}
