package com.cleancode.authservice.controller;

import com.cleancode.authservice.service.SecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secret")
public class SecretController {

    @Autowired
    private SecretService secretService;

    @GetMapping("/create")
    public String getSecret() {
        return secretService.CreateSecret();
    }
}
