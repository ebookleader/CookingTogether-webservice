package com.jeongeun.project.springboot.web.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductsReviewResponseDto {
    private String userName;
    private int year;
    private int month;
    private int day;
    private boolean rating1;
    private boolean rating2;
    private boolean rating3;
    private boolean rating4;
    private boolean rating5;
    private String content;

    public ProductsReviewResponseDto(String userName, int year, int month, int day, List<Boolean> ratingList, String content) {
        this.userName = userName;
        this.year = year;
        this.month = month;
        this.day = day;
        this.rating1 = ratingList.get(0);
        this.rating2 = ratingList.get(1);
        this.rating3 = ratingList.get(2);
        this.rating4 = ratingList.get(3);
        this.rating5 = ratingList.get(4);
        this.content = content;
    }
}
