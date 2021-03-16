package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.Products;
import com.jeongeun.project.springboot.domain.products.ProductsReview;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductsReviewSaveRequestDto {
    private Long pid;
    private String content;
    private double rating;

    @Builder
    public ProductsReviewSaveRequestDto(Long pid, String content, double rating) {
        this.pid = pid;
        this.content = content;
        this.rating = rating;
    }

    public ProductsReview toEntity(Long uid, Long rid) {
        return ProductsReview.builder()
                .userId(uid)
                .content(content)
                .rating(rating)
                .reservationId(rid)
                .build();
    }
}
