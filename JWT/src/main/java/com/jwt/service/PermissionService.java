package com.jwt.service;


import com.jwt.dtos.PermissionDto;

import java.util.List;

public interface PermissionService {

    PermissionDto createNewPermissionAssignToRole(PermissionDto permissionDto);

    PermissionDto getPermissionById(long permissionId);

    List<PermissionDto> getAllPermissionList();

    PermissionDto updatePermissionDetails(long permissionId,PermissionDto permissionDto);

    void deletePermissionDetails(long permissionId);
}
