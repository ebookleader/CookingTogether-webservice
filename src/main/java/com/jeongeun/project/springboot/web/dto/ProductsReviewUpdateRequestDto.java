package com.jeongeun.project.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductsReviewUpdateRequestDto {
    private String content;
    private double rating;

    @Builder
    public ProductsReviewUpdateRequestDto(String content, double rating) {
        this.content = content;
        this.rating = rating;
    }
}
