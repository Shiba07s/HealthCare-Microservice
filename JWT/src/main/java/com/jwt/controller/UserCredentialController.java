package com.jwt.controller;


import com.jwt.dtos.UserDto;
import com.jwt.entity.Permission;
import com.jwt.entity.UserCredential;
import com.jwt.request.UserCreateRequest;
import com.jwt.response.ApiResponse;
import com.jwt.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class UserCredentialController {

    private final UserCredentialService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUserList();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", user));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserDto>> createUser( @RequestBody UserCreateRequest createRequest) {
        UserDto createdUser = userService.createUser(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User created successfully", createdUser));
    }

    @PutMapping("/update-user-details/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUserDetails(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {

        UserDto updatedUser = userService.updateUserDetails(id, userDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", updatedUser));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UserDto>> updateUserStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {

        UserDto updatedUser = userService.updateUserStatus(id, active);
        return ResponseEntity.ok(new ApiResponse<>(true,
                active ? "User activated successfully" : "User deactivated successfully",
                updatedUser));
    }

    @PostMapping("/add/role-user/{userId}/roles/{roleId}")
    public ResponseEntity<ApiResponse<UserDto>> addRoleToUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        UserDto updatedUser = userService.addRoleToUser(userId, roleId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Role added to user successfully", updatedUser));
    }

    @DeleteMapping("/remove/role-user/{userId}/roles/{roleId}")
    public ResponseEntity<ApiResponse<UserDto>> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        UserDto updatedUser = userService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Role removed from user successfully", updatedUser));
    }

    @PatchMapping("/{username}/update-login-date")
    public ResponseEntity<ApiResponse<Void>> updateLastLoginDate(@PathVariable String username) {
        userService.updateLastLoginDate(username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Last login date updated successfully", null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
    }
}
