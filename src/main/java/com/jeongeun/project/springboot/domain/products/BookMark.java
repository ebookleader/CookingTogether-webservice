package com.jeongeun.project.springboot.domain.products;

import com.jeongeun.project.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.print.Book;

@Getter
@NoArgsConstructor
@Entity
@Table(name="BookMark")
public class BookMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, insertable = false)
    private Long bmid;

    @Column(nullable = false)
    private Long bmuid;

    @ManyToOne
    @JoinColumn(name="p_id")
    private Products products;

    @Builder
    public BookMark(Long bmuid) {
        this.bmuid = bmuid;
    }

    public void setProducts(Products products) {
        this.products = products;
    }


}
