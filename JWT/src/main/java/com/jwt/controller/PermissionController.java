package com.jwt.controller;

import com.jwt.dtos.PermissionDto;
import com.jwt.exception.ResourceAlreadyExistsException;
import com.jwt.exception.ResourceNotFoundException;
import com.jwt.response.ApiResponse;
import com.jwt.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/permission")
public class PermissionController {

    private final PermissionService permissionService;

    // 1. Create new permission
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PermissionDto>> createNewPermission(@RequestBody PermissionDto permissionDto) {
        try {
            PermissionDto created = permissionService.createNewPermissionAssignToRole(permissionDto);
            return new ResponseEntity<>(
                    ApiResponse.<PermissionDto>builder()
                            .success(true)
                            .message("Permission created successfully")
                            .data(created)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResourceAlreadyExistsException e) {
            return new ResponseEntity<>(
                    ApiResponse.<PermissionDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build(),
                    HttpStatus.CONFLICT
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.<PermissionDto>builder()
                            .success(false)
                            .message("Internal server error")
                            .data(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // 2. Get all permissions
    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionDto>>> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.getAllPermissionList();
        return ResponseEntity.ok(
                ApiResponse.<List<PermissionDto>>builder()
                        .success(true)
                        .message("Permission list fetched successfully")
                        .data(permissions)
                        .build()
        );
    }

    // 3. Get permission by ID
    @GetMapping("/{permissionId}")
    public ResponseEntity<ApiResponse<PermissionDto>> getPermissionById(@PathVariable long permissionId) {
        try {
            PermissionDto permission = permissionService.getPermissionById(permissionId);
            return ResponseEntity.ok(
                    ApiResponse.<PermissionDto>builder()
                            .success(true)
                            .message("Permission fetched successfully")
                            .data(permission)
                            .build()
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.<PermissionDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    // 4. Update permission
    @PutMapping("/update/{permissionId}")
    public ResponseEntity<ApiResponse<PermissionDto>> updatePermission(
            @PathVariable long permissionId,
            @RequestBody PermissionDto permissionDto
    ) {
        try {
            PermissionDto updated = permissionService.updatePermissionDetails(permissionId, permissionDto);
            return ResponseEntity.ok(
                    ApiResponse.<PermissionDto>builder()
                            .success(true)
                            .message("Permission updated successfully")
                            .data(updated)
                            .build()
            );
        } catch (ResourceNotFoundException | ResourceAlreadyExistsException e) {
            return new ResponseEntity<>(
                    ApiResponse.<PermissionDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build(),
                    e instanceof ResourceAlreadyExistsException ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND
            );
        }
    }

    // 5. Delete permission
    @DeleteMapping("/delete/{permissionId}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable long permissionId) {
        try {
            permissionService.deletePermissionDetails(permissionId);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .success(true)
                            .message("Permission deleted successfully")
                            .data(null)
                            .build()
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}
