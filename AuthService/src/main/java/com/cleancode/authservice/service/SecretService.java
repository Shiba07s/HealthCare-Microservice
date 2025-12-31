package com.cleancode.authservice.service;


import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class SecretService {

    public String CreateSecret() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[256];
        random.nextBytes(bytes);
        String secret = Base64.getEncoder().encodeToString(bytes);

        return secret;
    }
}
