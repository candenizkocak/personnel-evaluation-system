package com.workin.personnelevaluationsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Use @Controller for serving views
public class WebController {

    @GetMapping({"/", "/home"})
    public String homePage() {
        return "home"; // This resolves to /WEB-INF/views/home.jsp
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // This resolves to /WEB-INF/views/login.jsp
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        // This page will be accessible only after successful login
        return "dashboard"; // This resolves to /WEB-INF/views/dashboard.jsp
    }

    @GetMapping("/register")
    public String registerPage() {
        // We'll implement user registration later or keep it as a placeholder
        return "register"; // This resolves to /WEB-INF/views/register.jsp
    }
}