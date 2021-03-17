package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.ProductsQA;
import lombok.Getter;

@Getter
public class ProductsQAResponseDto {
    /* 질문 원본 관련 변수 */
    private Long qaId;
    private String userName;
    private int year;
    private int month;
    private int day;
    private String content;
    private boolean isSecret;
    private Long originNo;
    private int groupOder;
    private int groupLayer;
    /* 질문에 대한 답변 관련 변수*/
    // 답변이 있는지 없는지
    private boolean hasReply;
    // 답변 내용
    private String replyContent;
    // 답변 날짜
    private int replyYear;
    private int replyMonth;
    private int replyDay;


    public ProductsQAResponseDto(String userName, int year, int month, int day, boolean isSecret, ProductsQA productsQA,
                                 boolean hasReply, String replyContent, int replyYear, int replyMonth, int replyDay) {
        this.userName = userName;
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = productsQA.getContent();
        this.isSecret = isSecret;
        this.originNo = productsQA.getOriginNo();
        this.groupOder = productsQA.getGroupOrder();
        this.groupLayer = productsQA.getGroupLayer();
        this.qaId = productsQA.getQaId();
        this.hasReply = hasReply;
        this.replyContent = replyContent;
        this.replyYear = replyYear;
        this.replyMonth = replyMonth;
        this.replyDay = replyDay;
    }
}
