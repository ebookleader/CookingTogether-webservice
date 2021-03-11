package com.jeongeun.project.springboot.domain.products;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String fileName;

    private String fileOriginalName;

    private String fileUrl;

    private boolean isThumbnail;

    @ManyToOne
    @JoinColumn(name="p_id")
    private Products products;

    @Builder
    public Files(String fileName, String fileOriginalName, String fileUrl, boolean isThumbnail, Products products) {
        this.fileName = fileName;
        this.fileOriginalName = fileOriginalName;
        this.fileUrl = fileUrl;
        this.isThumbnail = isThumbnail;
        this.products = products;
    }
}
