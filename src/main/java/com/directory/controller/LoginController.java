package com.directory.controller;


import com.directory.service.test.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
public class LoginController {

    @GetMapping("/main")
    public String home(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) session.getAttribute("username");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.println(userDetails.getPassword());

        return "main"; // Home page after login
    }

    @GetMapping("/pages-login")
    public String login() {
        return "pages-login"; // Custom login page
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
}