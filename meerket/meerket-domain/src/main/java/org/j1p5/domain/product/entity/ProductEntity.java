package org.j1p5.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.j1p5.domain.global.entity.BaseEntity;

import java.awt.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@Table(name = "product")
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title" , nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false, length = 500, columnDefinition = "TEXT")
    private String content;

    @Column(name = "min_price", nullable = false)
    private int minPrice;

    @Column(name = "expired_time", nullable = false)
    private LocalDateTime expiredTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ProductCategory category;

    @Column(name = "address")
    private String address; // 물품 등록 상세 주소

    @Column(name = "location")
    private String location; // 물품 등록 ~시 ~구에대한 주소

    @Column(name = "coordinate", nullable = false, columnDefinition = "POINT SRID 4326")
    private Point coordinate;// 거래희망장소 ->물건의 좌표

    @Column(name = "is_early", nullable = false)
    private boolean isEarly = false;

    @Column(name = "has_buyer", nullable = false)
    private boolean hasBuyer = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", nullable = false)
    private ProductStatus status;

}
