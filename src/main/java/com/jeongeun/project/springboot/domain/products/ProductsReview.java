package com.jeongeun.project.springboot.domain.products;

import com.jeongeun.project.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ProductsReview extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    @ManyToOne
    @JoinColumn(name="p_id")
    private Products products;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long reservationId;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private double rating;

    @Builder
    public ProductsReview(Long userId, String content, double rating, Long reservationId) {
        this.userId = userId;
        this.content = content;
        this.rating = rating;
        this.reservationId = reservationId;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public void update(String content, double rating) {
        this.content = content;
        this.rating = rating;
    }


}
