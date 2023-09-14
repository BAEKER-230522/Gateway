package com.example.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectController {
    @GetMapping("/")
    public String conCheck() {
        return "200 Ok";
    }
}
