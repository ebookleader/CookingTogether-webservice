package com.jeongeun.project.springboot.web;

import com.jeongeun.project.springboot.config.auth.LoginUser;
import com.jeongeun.project.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLoginPage(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("userAccountName", user.getName());
        }
        return "account/login_user";
    }

}
