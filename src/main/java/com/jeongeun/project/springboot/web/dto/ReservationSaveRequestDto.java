package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.domain.reservation.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationSaveRequestDto {
    private int ryear;
    private int rmonth;
    private int rday;
    private int numOfPeople;
    private int totalPrice;
    private Long productId;
    private Long optionId;
    //sellerId, userId
    private String userReservationName;
    private String userReservationEmail;
    private String userReservationPhone;

    @Builder
    public ReservationSaveRequestDto(int ryear, int rmonth, int rday, int numOfPeople, int totalPrice,
                                     Long productId, Long optionId, String userReservationName,
                                     String userReservationEmail, String userReservationPhone) {
        this.ryear = ryear;
        this.rmonth = rmonth;
        this.rday = rday;
        this.numOfPeople = numOfPeople;
        this.totalPrice = totalPrice;
        this.productId = productId;
        this.optionId = optionId;
        this.userReservationName = userReservationName;
        this.userReservationEmail = userReservationEmail;
        this.userReservationPhone = userReservationPhone;
    }

    public Reservation toEntity(Long sellerId, Long userId) {
        return Reservation.builder()
                .ryear(ryear)
                .rmonth(rmonth)
                .rday(rday)
                .numOfPeople(numOfPeople)
                .totalPrice(totalPrice)
                .reservationStatus(ReservationStatus.APPLIED)
                .sellerId(sellerId)
                .userId(userId)
                .optionId(optionId)
                .userReservationName(userReservationName)
                .userReservationEmail(userReservationEmail)
                .userReservationPhone(userReservationPhone)
                .build();
    }
}
