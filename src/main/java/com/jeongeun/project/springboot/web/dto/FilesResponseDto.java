package com.jeongeun.project.springboot.web.dto;

import com.jeongeun.project.springboot.domain.products.Files;
import lombok.Getter;

@Getter
public class FilesResponseDto {
    private Long fileId;
    private String fileName;
    private String fileOriginalName;
    private String fileUrl;

    public FilesResponseDto(Files files) {
        this.fileId = files.getFileId();
        this.fileName = files.getFileName();
        this.fileOriginalName = files.getFileOriginalName();
        this.fileUrl = files.getFileUrl();
    }

}
