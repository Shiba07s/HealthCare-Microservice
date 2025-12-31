package com.jwt.service;

import com.jwt.dtos.RoleDto;
import com.jwt.entity.Permission;
import com.jwt.entity.Role;

import java.util.List;

public interface RoleService {

    RoleDto createNewRole(RoleDto roleDto);

    RoleDto getRoleById(long roleId);

    List<RoleDto> getAllRoleList();

    RoleDto updateRole(RoleDto roleDto,long roleId);

    void deleteRole(long roleId);

    RoleDto addPermissionToRole(Long roleId, Long permissionId);

    RoleDto removePermissionFromRole(Long roleId, Long permissionId);
}
