package com.jwt.controller;


import com.jwt.dtos.RoleDto;
import com.jwt.entity.Role;
import com.jwt.exception.ResourceAlreadyExistsException;
import com.jwt.exception.ResourceNotFoundException;
import com.jwt.repository.RoleRepository;
import com.jwt.response.ApiResponse;
import com.jwt.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/role")
public class RoleController {

    private final RoleService roleService;
    private final RoleRepository roleRepository;

    // 1. Create Role
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RoleDto>> createNewRole(@RequestBody RoleDto roleDto) {
        try {
            RoleDto newRole = roleService.createNewRole(roleDto);
            return ResponseEntity.ok(
                    ApiResponse.<RoleDto>builder()
                            .success(true)
                            .message("Role is created successfully!")
                            .data(newRole)
                            .build()
            );
        } catch (ResourceAlreadyExistsException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.<RoleDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<RoleDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRoleList() {
        List<RoleDto> allRoleList = roleService.getAllRoleList();
        return ResponseEntity.ok(
                ApiResponse.<List<RoleDto>>builder()
                        .success(true)
                        .message("roles data fetched sucessfully")
                        .data(allRoleList)
                        .build()
        );
    }

    // 3. Get role by ID
    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleDto>> getRoleById(@PathVariable long roleId) {
        try {
            RoleDto roleById = roleService.getRoleById(roleId);
            return ResponseEntity.ok(
                    ApiResponse.<RoleDto>builder()
                            .success(true)
                            .message("Role fetched successfully")
                            .data(roleById)
                            .build()
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<RoleDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }

    // 4. Get role by name
    @GetMapping("/rolename/{roleName}")
    public ResponseEntity<ApiResponse<Role>> getRoleByName(@PathVariable String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Role>builder()
                            .success(false)
                            .message("Role not found with name: " + roleName)
                            .data(null)
                            .build()
            );
        }
        return ResponseEntity.ok(
                ApiResponse.<Role>builder()
                        .success(true)
                        .message("Role fetched successfully")
                        .data(role)
                        .build()
        );
    }

    @PutMapping("/update/{roleId}")
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(@RequestBody RoleDto roleDto, @PathVariable long roleId) {
        try {
            RoleDto updatedRole = roleService.updateRole(roleDto, roleId);
            return ResponseEntity.ok(
                    ApiResponse.<RoleDto>builder()
                            .success(true)
                            .message("Role updated successfully")
                            .data(updatedRole)
                            .build()
            );
        } catch (ResourceNotFoundException | ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.<RoleDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }

    // 6. Delete role
    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable long roleId) {
        try {
            roleService.deleteRole(roleId);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .success(true)
                            .message("Role deleted successfully")
                            .data(null)
                            .build()
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Void>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }

    @PostMapping("/add/{roleId}/permissions/{permissionId}")
    public ResponseEntity<ApiResponse<RoleDto>> addPermissionToRole(
             @PathVariable Long roleId,
            @PathVariable Long permissionId) {

        RoleDto updatedRole = roleService.addPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Permission added to role successfully", updatedRole));
    }

    @DeleteMapping("/remove/{roleId}/permissions/{permissionId}")
    public ResponseEntity<ApiResponse<RoleDto>> removePermissionFromRole(
             @PathVariable Long roleId,
            @PathVariable Long permissionId) {

        try {
            RoleDto updatedRole = roleService.removePermissionFromRole(roleId, permissionId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Permission removed from role successfully", updatedRole));
        }catch ( Exception e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(),null));

        }

    }


}
