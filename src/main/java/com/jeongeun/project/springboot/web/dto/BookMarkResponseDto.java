package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.BookMark;
import lombok.Getter;

@Getter
public class BookMarkResponseDto {
    private int idx;
    private Long bmid;
    private Long p_id;
    private String p_name;

    public BookMarkResponseDto(int idx, BookMark bookMark, String p_name) {
        this.idx = idx;
        this.bmid = bookMark.getBmid();
        this.p_id = bookMark.getProducts().getP_id();
        this.p_name = p_name;
    }
}
