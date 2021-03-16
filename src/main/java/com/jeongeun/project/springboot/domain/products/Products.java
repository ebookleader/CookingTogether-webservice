package com.jeongeun.project.springboot.domain.products;

import com.jeongeun.project.springboot.domain.BaseTimeEntity;
import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Products extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    @Column(name="p_id", updatable = false, insertable = false)
    private Long p_id; //bigint

    @ManyToOne
    @JoinColumn(name="id")
    private User user;

    @Column(nullable = false, length = 100, name = "p_name")
    private String productsName;

    @Column(nullable = false)
    private int p_postcode;

    @Column(nullable = false)
    private String p_address;

    @Column(nullable = false)
    private String p_detailAddress;

    @Column(nullable = false, name = "p_city")
    private String productsCity;

    @Column(nullable = false, name = "p_weekday_price")
    private int weekdayPrice;

    @Column(nullable = false, name="p_weekend_price")
    private int weekendPrice;

    @Column(length = 500)
    private String p_introduce;

    private int p_maxNum;
    private int p_liked;

    @Column(name = "p_avg_rating")
    private double productsAvgRating;

    @OneToMany(mappedBy = "products", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProductsFacility> facility = new ArrayList<>();

    @OneToMany(mappedBy = "products")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProductsNotice> p_notice = new ArrayList<>();

    @OneToMany(mappedBy = "products")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProductsPolicy> p_policy = new ArrayList<>();

    @OneToMany(mappedBy = "products")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProductsOption> p_option = new ArrayList<>();

    @OneToMany(mappedBy = "products")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "products")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Files> files = new ArrayList<>();

    @OneToMany(mappedBy = "products")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<BookMark> bookMarks = new ArrayList<>();



    @Builder
    public Products(String p_name, int p_postcode, String p_address, String p_detailAddress, String p_city,
                    int p_weekdayPrice, int p_weekendPrice, String p_introduce,  int p_maxNum, int p_liked, double p_avgRating
                    ) {
        this.productsName = p_name;
        this.p_postcode = p_postcode;
        this.p_address = p_address;
        this.p_detailAddress = p_detailAddress;
        this.productsCity = p_city;
        this.weekdayPrice = p_weekdayPrice;
        this.weekendPrice = p_weekendPrice;
        this.p_introduce = p_introduce;
        this.p_maxNum = p_maxNum;
        this.p_liked = p_liked;
        this.productsAvgRating = p_avgRating;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void update(String p_name, int p_postcode, String p_address, String p_detailAddress, String p_city, int p_weekdayPrice,
                       int p_weekendPrice, String p_introduce, int p_maxNum) {
        this.productsName = p_name;
        this.p_postcode = p_postcode;
        this.p_address = p_address;
        this.p_detailAddress = p_detailAddress;
        this.productsCity = p_city;
        this.weekdayPrice = p_weekdayPrice;
        this.weekendPrice = p_weekendPrice;
        this.p_introduce = p_introduce;
        this.p_maxNum = p_maxNum;
    }

    public Long getSellerId() {
        return this.user.getId();
    }


}
