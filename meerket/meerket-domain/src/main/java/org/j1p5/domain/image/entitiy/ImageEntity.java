package org.j1p5.domain.image.entitiy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.j1p5.domain.global.entity.BaseEntity;
import org.j1p5.domain.product.entity.ProductEntity;

@Entity(name = "image")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "image_url", nullable = false, length = 2048)
    private String imageUrl;

    private ImageEntity(ProductEntity product, String imageUrl) {
        this.product = product;
        this.imageUrl = imageUrl;
    }

    public static ImageEntity from(String imageUrl) {
        return new ImageEntity(null, imageUrl);
    }

    public static ImageEntity of(ProductEntity product, String imageUrl) {
        return new ImageEntity(null, product, imageUrl);
    }
}
