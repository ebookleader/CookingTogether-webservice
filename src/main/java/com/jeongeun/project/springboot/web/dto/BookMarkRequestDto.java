package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.BookMark;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BookMarkRequestDto {
    private Long bmuid;

    @Builder
    public BookMarkRequestDto(Long bmuid) {
        this.bmuid = bmuid;
    }

    public BookMark toEntity() {
        return BookMark.builder()
                .bmuid(this.bmuid)
                .build();
    }
}
