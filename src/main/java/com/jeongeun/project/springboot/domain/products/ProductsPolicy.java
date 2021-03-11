package com.jeongeun.project.springboot.domain.products;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ProductsPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pp_id;

    @ManyToOne
    @JoinColumn(name="p_id")
    private Products products;

    private String p_policy;

    @Builder
    public ProductsPolicy(String p_policy, Products products) {
        this.p_policy = p_policy;
        this.products = products;
    }
}
