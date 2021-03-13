package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.Products;
import lombok.Getter;

@Getter
public class ProductsResponseDto {
    private Long p_id;
    private String p_name;
    private int p_postcode;
    private String p_address;
    private String p_detailAddress;
    private String p_city;
    private int p_weekdayPrice;
    private int p_weekendPrice;
    private String p_introduce;
    private int p_maxNum;
    private int p_liked;
    private double p_avgRating;

    public ProductsResponseDto(Products entity) {
        this.p_id = entity.getP_id();
        this.p_name = entity.getProductsName();
        this.p_postcode = entity.getP_postcode();
        this.p_address = entity.getP_address();
        this.p_detailAddress = entity.getP_detailAddress();
        this.p_city = entity.getProductsCity();
        this.p_weekdayPrice = entity.getWeekdayPrice();
        this.p_weekendPrice = entity.getWeekendPrice();
        this.p_introduce = entity.getP_introduce();
        this.p_maxNum = entity.getP_maxNum();
        this.p_liked = entity.getP_liked();
        this.p_avgRating = entity.getProductsAvgRating();
    }
}
