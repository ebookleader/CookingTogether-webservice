package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.domain.reservation.ReservationStatus;
import lombok.Getter;

@Getter
public class ReservationResponseDto {
    private Long rid;
    private int ryear;
    private int rmonth;
    private int rday;
    private int numOfPeople;
    private int totalPrice;
    private Long pid;
    private String pname;
    private ReservationStatus reservationStatus;
    private Long optionId;
    private String userReservationName;
    private String userReservationEmail;
    private String userReservationPhone;

    public ReservationResponseDto(Reservation entity) {
        this.rid = entity.getRid();
        this.ryear = entity.getRyear();
        this.rmonth = entity.getRmonth();
        this.rday = entity.getRday();
        this.numOfPeople = entity.getNumOfPeople();
        this.totalPrice = entity.getTotalPrice();
        this.pid = entity.getProducts().getP_id();
        this.pname = entity.getProducts().getP_name();
        this.reservationStatus = entity.getReservationStatus();
        this.optionId = entity.getOptionId();
        this.userReservationName = entity.getUserReservationName();
        this.userReservationEmail = entity.getUserReservationEmail();
        this.userReservationPhone = entity.getUserReservationPhone();
    }
}
