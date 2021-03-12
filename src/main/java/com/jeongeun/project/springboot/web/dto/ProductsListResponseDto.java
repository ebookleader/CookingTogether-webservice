package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.Products;
import lombok.Getter;

@Getter
public class ProductsListResponseDto {
    private Long p_id;
    private String p_name;
    private double p_avgRating;
    private int p_weekdayPrice;
    private String p_city;

    public ProductsListResponseDto(Products entity) {
        this.p_id = entity.getP_id();
        this.p_avgRating = entity.getP_avgRating();
        this.p_name = entity.getP_name();
        this.p_weekdayPrice = entity.getP_weekdayPrice();
        this.p_city = entity.getP_city();
    }
}
