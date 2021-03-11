package com.jeongeun.project.springboot.web.dto;

import lombok.Getter;

@Getter
public class PageListResponseDto {
    private int pageNum;
    private boolean pageActive;

    public PageListResponseDto(int no, boolean pageActive) {
        this.pageNum = no;
        this.pageActive = pageActive;
    }
}
