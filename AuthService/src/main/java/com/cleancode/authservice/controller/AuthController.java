package com.cleancode.authservice.controller;


import com.cleancode.authservice.dtos.AuthRequest;
import com.cleancode.authservice.entity.UserCredential;
import com.cleancode.authservice.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserCredentialService userCredentialService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<UserCredential> addUserCredential(@RequestBody UserCredential userCredential) {
        UserCredential newUserCredential = userCredentialService.createNewUserCredential(userCredential);
        return new ResponseEntity<>(newUserCredential, HttpStatus.CREATED);
    }


    @GetMapping("/signin")
    public ResponseEntity<String> getAuthRequest(@RequestBody AuthRequest authRequest) {
        logger.info("authRequest: {}", authRequest);

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authenticate.isAuthenticated()) {
            String generateToken = userCredentialService.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(generateToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Access");
        }
    }


    @GetMapping("/validate")
    public void validateToken(@RequestParam("token") String token) {
         userCredentialService.validateToken(token);

    }

}
