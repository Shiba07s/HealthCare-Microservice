package com.cleancode.authservice.service;

import com.cleancode.authservice.entity.UserCredential;

public interface UserCredentialService {

    UserCredential createNewUserCredential(UserCredential userCredential);

    String generateToken(String userName);

    void validateToken(String token);
}
