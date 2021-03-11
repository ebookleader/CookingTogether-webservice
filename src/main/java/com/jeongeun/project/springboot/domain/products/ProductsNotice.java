package com.jeongeun.project.springboot.domain.products;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ProductsNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pn_id;

    @ManyToOne
    @JoinColumn(name="p_id")
    private Products products;

    private String p_notice;

    @Builder
    public ProductsNotice(String p_notice, Products products) {
        this.p_notice = p_notice;
        this.products = products;
    }
}
