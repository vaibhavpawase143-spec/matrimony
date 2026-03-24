package com.example.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "demo";   // MUST match demo.html
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}