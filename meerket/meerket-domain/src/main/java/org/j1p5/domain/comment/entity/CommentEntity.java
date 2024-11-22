package org.j1p5.domain.comment.entity;

import jakarta.persistence.*;
import org.j1p5.domain.global.entity.BaseEntity;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.user.entity.UserEntity;

@Entity
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "conent", nullable = false)
    private String content;

    @Column(name = "parent_id")
    private Long parentId=null;

    @Column(name = "comment_status")
    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}
