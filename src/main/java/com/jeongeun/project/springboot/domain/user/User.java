package com.jeongeun.project.springboot.domain.user;

import com.jeongeun.project.springboot.domain.BaseTimeEntity;
import com.jeongeun.project.springboot.domain.products.Products;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, insertable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean isSeller;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Products> products = new ArrayList<>();


    @Builder
    public User(String name, String email, String picture, boolean emailVerified, Role role, boolean isSeller) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.emailVerified = emailVerified;
        this.role = role;
        this.isSeller = isSeller;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public void updateToSeller() {
        this.role = Role.SELLER;
        this.isSeller = true;
    }

    public void updateToUser() {
        this.role = Role.USER;
    }

    public void undoEnrollSeller() {
        this.role = Role.USER;
        this.isSeller = false;
    }

    public void updateEmailVerified() {
        this.emailVerified = true;
    }

    public void updateVerificationCode(String code) {
        this.verificationCode = code;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
