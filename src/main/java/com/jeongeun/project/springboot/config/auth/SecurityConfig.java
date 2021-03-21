package com.jeongeun.project.springboot.config.auth;

import com.jeongeun.project.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable() //h2-console 사용
                .and()
                    .authorizeRequests() //url별 권환 관리, antMatchers 옵션 사용
                    .antMatchers("/", "/css/**", "/img/**", "/js/**", "/h2-console/**", "/basicUser/**", "/login").permitAll()
                    .antMatchers( "/api/guestUser/**").hasRole(Role.GUEST.name())
                    .antMatchers( "/space/list/**", "/api/user/**").access("hasRole('USER') or hasRole('SELLER')")
                    .antMatchers("/api/allUser/deleteAccount").access("hasRole('GUEST') or hasRole('USER') or hasRole('SELLER')")
                    .antMatchers("/seller/**").hasRole(Role.USER.name())
                    .antMatchers("/api/v1/**", "/space/save/**", "/space/update/**", "/isSeller/**").hasRole(Role.SELLER.name())
                    .anyRequest().authenticated()
                .and()
                    .oauth2Login().loginPage("/login")
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                    .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService); //소셜 로그인 성공시 후속조치 진행
    }
}
