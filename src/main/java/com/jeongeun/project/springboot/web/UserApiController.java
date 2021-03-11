package com.jeongeun.project.springboot.web;

import com.jeongeun.project.springboot.config.auth.LoginUser;
import com.jeongeun.project.springboot.config.auth.dto.SessionUser;
import com.jeongeun.project.springboot.domain.user.Role;
import com.jeongeun.project.springboot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final UserService userService;
    private final JavaMailSender javaMailSender;

    // enroll seller
    @GetMapping("/api/user/checkIsSeller")
    public boolean checkIsSeller(@LoginUser SessionUser user) {
        boolean result;
        if(user.getRole().equals(Role.SELLER))
            result = true;
        else
            result = false;
        return result;
    }

    @PutMapping("/api/user/enrollSeller")
    public Long updateUserSeller(@LoginUser SessionUser user) {
        String userEmail = user.getEmail();
        return userService.updateToSeller(userEmail);
    }

    @PutMapping("/api/user/undo/enrollSeller")
    public Long undoEnrollSeller(@LoginUser SessionUser user) {
        String email = user.getEmail();
        return userService.undoEnrollSeller(email);
    }

    // email verify
    @PostMapping("/api/guestUser/email/verify")
    public Long sendMail(String userEmail) {
        return userService.sendVerificationMail(userEmail);
    }

    @PutMapping("/api/guestUser/email/check")
    public boolean checkEmailCode(String inputKey, String userEmail) {
        boolean result = userService.checkEmailCode(inputKey, userEmail);
        return result;
    }
}
