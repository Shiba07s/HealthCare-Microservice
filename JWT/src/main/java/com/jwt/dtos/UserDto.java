package com.jwt.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private boolean active;
    private LocalDateTime lastLoginDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<RoleDto> roles;
}