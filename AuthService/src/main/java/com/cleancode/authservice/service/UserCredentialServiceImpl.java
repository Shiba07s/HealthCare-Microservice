package com.cleancode.authservice.service;

import com.cleancode.authservice.entity.UserCredential;
import com.cleancode.authservice.repository.UserCredentialRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @Override
    public UserCredential createNewUserCredential(UserCredential userCredential) {
        String encode = passwordEncoder.encode(userCredential.getPassword());
        userCredential.setPassword(encode);
         return userCredentialRepository.save(userCredential);
    }

    @Override
    public String generateToken(String userName) {
        return jwtService.generateToken(userName);

    }

    @Override
    public void validateToken(String token) {
          jwtService.validateToken(token);
    }
}
