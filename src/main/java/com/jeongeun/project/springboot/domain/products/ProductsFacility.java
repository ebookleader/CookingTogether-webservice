package com.jeongeun.project.springboot.domain.products;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ProductsFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pf_id;

//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ManyToOne // facility 저장시 product 저장하지 않도록 cascade 설정x
    @JoinColumn(name="p_id")
    private Products products;

    private String p_facility;


    @Builder
    public ProductsFacility(String p_facility, Products products) {
        this.p_facility = p_facility;
        this.products = products;
    }
}
