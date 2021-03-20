package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.ProductsOption;
import com.jeongeun.project.springboot.domain.products.ProductsReview;
import com.jeongeun.project.springboot.domain.reservation.Reservation;
import lombok.Getter;

import java.util.List;

@Getter
public class UserReviewListResponseDto {
    // 상품명, 예약 정보
    private String pname;
    private int reserveYear;
    private int reserveMonth;
    private int reserveDay;
    private String optionTitle;
    private int startTime;
    private int endTime;
    // 리뷰 정보
    private String userName;
    private Long reviewId;
    private int reviewYear;
    private int reviewMonth;
    private int reviewDay;
    private boolean rating1;
    private boolean rating2;
    private boolean rating3;
    private boolean rating4;
    private boolean rating5;
    private String content;

    public UserReviewListResponseDto(String pname, Reservation reservation, ProductsOption option, String userName, ProductsReview review, List<Boolean> ratingList) {
        this.pname = pname;
        this.reserveYear = reservation.getRyear();
        this.reserveMonth = reservation.getRmonth();
        this.reserveDay = reservation.getRday();
        this.optionTitle = option.getOptionTitle();
        this.startTime = option.getStartTime();
        this.endTime = option.getEndTime();
        this.userName = userName;
        this.reviewId = review.getReviewId();
        this.reviewYear = review.getModifiedDate().getYear();
        this.reviewMonth = review.getModifiedDate().getMonthValue();
        this.reviewDay = review.getModifiedDate().getDayOfMonth();
        this.rating1 = ratingList.get(0);
        this.rating2 = ratingList.get(1);
        this.rating3 = ratingList.get(2);
        this.rating4 = ratingList.get(3);
        this.rating5 = ratingList.get(4);
        this.content = review.getContent();
    }
}
