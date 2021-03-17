package com.jeongeun.project.springboot.domain.products;

import com.jeongeun.project.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ProductsQA extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long qaId;

    @ManyToOne
    @JoinColumn(name = "p_id")
    private Products products;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String content;

    /* 원글 번호 */
    private Long originNo;

    /* 원글 내에서의 순서 */
    @Column(nullable = false)
    private int groupOrder;

    /* 답글 계층(원글에대한 답글인지, 답글에대한 답글인지) */
    @Column(nullable = false)
    private int groupLayer;

    /* 0=false(공개) 1=true(비공개) */
    @Column(nullable = false)
    private int isSecret;


    @Builder
    public ProductsQA(Long userId, String content, int groupOrder, int groupLayer, int isSecret) {
        this.userId = userId;
        this.content = content;
        this.groupOrder = groupOrder;
        this.groupLayer = groupLayer;
        this.isSecret = isSecret;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
    public void setOriginNo(Long originNo) {
        this.originNo = originNo;
    }
}
