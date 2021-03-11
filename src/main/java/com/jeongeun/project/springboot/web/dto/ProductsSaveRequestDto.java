package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.Products;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductsSaveRequestDto {
    private String p_name;
    private int p_postcode;
    private String p_address;
    private String p_detailAddress;
    private String p_city;
    private String p_category;
    private int p_weekdayPrice;
    private int p_weekendPrice;
    private String p_introduce;
    private int p_maxNum;
    private int p_liked;
    private double p_avgRating;
    private List<String> facility;
    private List<String> notice;
    private List<String> policy;

    private List<String> optionTitle;
    private List<Integer> startTime;
    private List<Integer> endTime;
    private List<Integer> count;


    @Builder
    public ProductsSaveRequestDto(String p_name, int p_postcode, String p_address, String p_detailAddress, String p_city, String p_category, int p_weekdayPrice, int p_weekendPrice, String p_introduce,
                                  int p_maxNum, int p_liked, double p_avgRating, List<String> facility, List<String> notice, List<String> policy, List<String> optionTitle,
                                  List<Integer> startTime, List<Integer> endTime, List<Integer> count) {
        this.p_name = p_name;
        this.p_postcode = p_postcode;
        this.p_address = p_address;
        this.p_detailAddress = p_detailAddress;
        this.p_city = p_city;
        this.p_category = p_category;
        this.p_weekdayPrice = p_weekdayPrice;
        this.p_weekendPrice = p_weekendPrice;
        this.p_introduce = p_introduce;
        this.p_maxNum = p_maxNum;
        this.p_liked = p_liked;
        this.p_avgRating = p_avgRating;
        this.facility = facility;
        this.notice = notice;
        this.policy = policy;
        this.optionTitle = optionTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.count = count;
    }

    public Products toEntity() {
        return Products.builder()
                .p_name(p_name)
                .p_postcode(p_postcode)
                .p_address(p_address)
                .p_detailAddress(p_detailAddress)
                .p_city(p_city)
                .p_category(p_category)
                .p_weekdayPrice(p_weekdayPrice)
                .p_weekendPrice(p_weekendPrice)
                .p_introduce(p_introduce)
                .p_maxNum(p_maxNum)
                .p_liked(p_liked)
                .p_avgRating(p_avgRating)
                .build();
    }

}
