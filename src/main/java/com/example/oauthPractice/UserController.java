package com.example.oauthPractice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authFacade;

    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

}

