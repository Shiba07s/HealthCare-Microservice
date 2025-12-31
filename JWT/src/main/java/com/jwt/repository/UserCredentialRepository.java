package com.jwt.repository;

import com.jwt.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

 import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserCredential> findByUsername(String username);
}
