package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.ProductsPolicy;
import lombok.Getter;

@Getter
public class ProductsPolicyResponseDto {
    private Long pp_id;
    private String p_policy;

    public ProductsPolicyResponseDto(ProductsPolicy entity) {
        this.pp_id = entity.getPp_id();
        this.p_policy = entity.getP_policy();
    }
}
