package org.j1p5.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.j1p5.domain.global.entity.BaseEntity;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.user.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
@BatchSize(size = 10)
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "parentComment")
    @Builder.Default
    private List<CommentEntity> replies = new ArrayList<>(); //부모댓글이 가진 대댓글 리스트 가져오기 ->이를 통해 부모댓글이 자식댓글들 가져올 수 있게함

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommentEntity parentComment = null;

    @Column(name = "comment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    public void updateContent(String content){
        this.content = content;
        this.status = CommentStatus.UPDATED;
    }

    public void updateStatusDelete(){
        this.status = CommentStatus.DELETED;
    }
}