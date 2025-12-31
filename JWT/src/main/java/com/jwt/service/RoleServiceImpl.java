package com.jwt.service;


import com.jwt.dtos.RoleDto;
import com.jwt.entity.Permission;
import com.jwt.entity.Role;
import com.jwt.exception.ResourceAlreadyExistsException;
import com.jwt.exception.ResourceNotFoundException;
import com.jwt.repository.PermissionRepository;
import com.jwt.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    //1. create new Role
    @Override
    public RoleDto createNewRole(RoleDto roleDto) {

        Role map = modelMapper.map(roleDto, Role.class);

        if ( roleRepository.existsByName(roleDto.getName())){
            throw new ResourceAlreadyExistsException("Role is already exists with this name : "+roleDto.getName());
        }
        Set<Permission> resolvedPermissions = map.getPermissions().stream()
                .map(p -> {
                    Permission permission = permissionRepository.findByName(p.getName());
                    if (permission == null) {
                        throw new RuntimeException("Permission not found: " + p.getName());
                    }
                    return permission;
                })
                .collect(Collectors.toSet());

        map.setPermissions(resolvedPermissions);

        Role save = roleRepository.save(map);
        return modelMapper.map(save,RoleDto.class);
    }

    //2. get Role Details by id.
    @Override
    public RoleDto getRoleById(long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role is not found with this id : " + roleId));
        return modelMapper.map(role,RoleDto.class);
    }

    // 3. get All Role Details
    @Override
    public List<RoleDto> getAllRoleList() {
        List<Role> all = roleRepository.findAll();
        return all.stream().map(roles->modelMapper.map(roles,RoleDto.class)).collect(Collectors.toList());
    }

    //4. update the role
    @Override
    public RoleDto updateRole(RoleDto roleDto, long roleId) {
        Role existingRole = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role is not found with this id : " + roleId));


        if (!existingRole.getName().equals(roleDto.getName()) && roleRepository.existsByName(roleDto.getName())){
            throw new ResourceAlreadyExistsException("Role is already exists with this name : "+roleDto.getName());
        }

        Set<Permission> collect = roleDto.getPermissions().stream()
                .map(roleUpdate -> {
                    Permission permissionName = permissionRepository.findByName(roleUpdate.getName());
                    if (permissionName == null) {
                        throw new RuntimeException("Permission not found: " + roleUpdate.getName());
                    }
                    return permissionName;
                }).collect(Collectors.toSet());

        existingRole.setName(roleDto.getName());
        existingRole.setDescription(roleDto.getDescription());
        existingRole.setPermissions(collect);
        Role save = roleRepository.save(existingRole);
        return modelMapper.map(save,RoleDto.class);
    }

    //5. delete the role
    @Override
    public void deleteRole(long roleId) {
        Role existingRole = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role is not found with this id : " + roleId));

        roleRepository.delete(existingRole);
    }

    @Transactional
    @Override
    public RoleDto addPermissionToRole(Long roleId, Long permissionId) {

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role is not found with this id : " + roleId));
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException("Permission is not found with this id : " + permissionId));

        role.getPermissions().add(permission);
        Role save = roleRepository.save(role);
        return modelMapper.map(save,RoleDto.class);

    }

    @Transactional
    @Override
    public RoleDto removePermissionFromRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + permissionId));


        if (!role.getPermissions().contains(permission)) {
            throw new ResourceNotFoundException("Permission with id: " + permissionId + " is not assigned to the role");
        }

        role.getPermissions().remove(permission);
        role = roleRepository.save(role);

        return modelMapper.map(role,RoleDto.class);
    }

}
