package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.Products;
import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.domain.reservation.ReservationStatus;
import lombok.Getter;

@Getter
public class ReservationListResponseDto {
    private Long pid;
    private String p_name;
    private Long rid;
    private ReservationStatus status;
    private String userReservationName;
    private int ryear;
    private int rmonth;
    private int rday;
    private boolean hasReview;

    public ReservationListResponseDto(ReservationResponseDto reservation, boolean hasReview) {
        this.pid = reservation.getPid();
        this.p_name = reservation.getPname();
        this.rid = reservation.getRid();
        this.status = reservation.getReservationStatus();
        this.userReservationName = reservation.getUserReservationName();
        this.ryear = reservation.getRyear();
        this.rmonth = reservation.getRmonth();
        this.rday = reservation.getRday();
        this.hasReview = hasReview;
    }

}
