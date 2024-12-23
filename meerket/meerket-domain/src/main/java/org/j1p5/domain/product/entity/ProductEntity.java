package org.j1p5.domain.product.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.j1p5.domain.global.entity.BaseEntity;
import org.j1p5.domain.image.entitiy.ImageEntity;
import org.j1p5.domain.product.dto.ProductUpdateInfo;
import org.j1p5.domain.user.entity.UserEntity;
import org.locationtech.jts.geom.Point;

@Entity(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "title", nullable = false)
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
    private Point coordinate; // 거래희망장소 ->물건의 좌표

    @Column(name = "is_early", nullable = false)
    private boolean isEarly = false;

    @Column(name = "has_buyer", nullable = false)
    private boolean hasBuyer = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", nullable = false)
    private ProductStatus status;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ImageEntity> imageEntityList = new ArrayList<>(); // 이미지와 단방향관계로 설정

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "winning_price")
    private Integer winningPrice;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;

    public void addImage(ImageEntity image) {
        imageEntityList.add(image);
    }

    public void removeImage(ImageEntity image) {
        imageEntityList.remove(image);
    }

    public void updateProduct(ProductUpdateInfo productUpdateInfo, Point coordinate) {

        if (productUpdateInfo.title() != null) {
            this.title = productUpdateInfo.title();
        }
        if (productUpdateInfo.content() != null) {
            this.content = productUpdateInfo.content();
        }
        if (productUpdateInfo.minimumPrice() != null) {
            this.minPrice = productUpdateInfo.minimumPrice();
        }
        if (productUpdateInfo.category() != null) {
            this.category = productUpdateInfo.category();
        }
        if (coordinate != null) {
            this.coordinate = coordinate;
        }
        if (address != null) {
            this.address = address;
        }
        if (productUpdateInfo.content() != null) {
            this.content = productUpdateInfo.content();
        }
    }

    public void updateStatusToDelete(ProductEntity product) {
        this.status = ProductStatus.DELETED;
    }

    public void updateStatusToInProgress() {
        this.status = ProductStatus.IN_PROGRESS;
    }

    public void createThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void updateIsEarly() {
        this.isEarly = true;
    }

    public void updateExpiredTime() {
        //        this.expiredTime = LocalDateTime.now().plus(2, ChronoUnit.HOURS);
        this.expiredTime = LocalDateTime.now().plus(10, ChronoUnit.SECONDS); // 시연을 위한 20초
    }

    public void updateWinningPrice(int winningPrice) {
        this.winningPrice = winningPrice;
    }

    public void updateHasBuyer() {
        this.hasBuyer = true;
    }

    public void updateHasBuyerFalse() {
        this.hasBuyer = false;
    }

    public void updateStatusToComplete() {
        this.status = ProductStatus.COMPLETED;
    }

    public void withdraw() {
        this.isDeleted = true;
    }
}
