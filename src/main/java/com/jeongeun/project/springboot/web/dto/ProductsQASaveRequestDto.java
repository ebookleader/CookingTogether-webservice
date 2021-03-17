package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.ProductsQA;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductsQASaveRequestDto {
    private Long pid;
    private String content;
    private int isSecret;

    @Builder
    public ProductsQASaveRequestDto(Long pid, String content, int isSecret) {
        this.pid = pid;
        this.content = content;
        this.isSecret = isSecret;
    }

    public ProductsQA toEntity(Long uid, int groupOrder, int groupLayer) {
        return ProductsQA.builder()
                .userId(uid)
                .content(content)
                .groupOrder(groupOrder)
                .groupLayer(groupLayer)
                .isSecret(isSecret)
                .build();
    }


}
