package com.jeongeun.project.springboot.domain.reservation;

import com.jeongeun.project.springboot.domain.BaseTimeEntity;
import com.jeongeun.project.springboot.domain.products.Products;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid", updatable = false, insertable = false)
    private Long rid;

    @Column(nullable = false)
    private int ryear;

    @Column(nullable = false)
    private int rmonth;

    @Column(nullable = false)
    private int rday;

    @Column(nullable = false)
    private int numOfPeople; //사람 수

    @Column(nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "reservation_status")
    private ReservationStatus reservationStatus;

    @ManyToOne
    @JoinColumn(name = "p_id")
    private Products products;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private String userReservationName;

    @Column(nullable = false)
    private String userReservationEmail;

    @Column(nullable = false)
    private String userReservationPhone;

    @Builder
    public Reservation(int ryear, int rmonth, int rday, int numOfPeople, int totalPrice,
                         ReservationStatus reservationStatus, Long sellerId,
                         Long userId, Long optionId, String userReservationName,
                         String userReservationEmail, String userReservationPhone) {
        this.ryear = ryear;
        this.rmonth = rmonth;
        this.rday = rday;
        this.numOfPeople = numOfPeople;
        this.totalPrice = totalPrice;
        this.reservationStatus = reservationStatus;
        this.sellerId = sellerId;
        this.userId = userId;
        this.optionId = optionId;
        this.userReservationName = userReservationName;
        this.userReservationEmail = userReservationEmail;
        this.userReservationPhone = userReservationPhone;
    }

    public void setProducts(Products products) {
        this.products = products;
    }


}
