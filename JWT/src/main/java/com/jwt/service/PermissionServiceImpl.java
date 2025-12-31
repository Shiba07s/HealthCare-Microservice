package com.jwt.service;


import com.jwt.dtos.PermissionDto;
import com.jwt.entity.Permission;
import com.jwt.exception.ResourceAlreadyExistsException;
import com.jwt.exception.ResourceNotFoundException;
import com.jwt.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    //1. get all permission
    @Override
    public List<PermissionDto> getAllPermissionList() {
        List<Permission> all = permissionRepository.findAll();
        return all.stream().map(permission -> modelMapper.map(permission,PermissionDto.class)).collect(Collectors.toList());
    }

    //2. get permission by id
    @Override
    public PermissionDto getPermissionById(long permissionId) {
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException("permission id is not present with this id: " + permissionId));
    return modelMapper.map(permission,PermissionDto.class);
    }

    //3. create new permission

    @Transactional
    @Override
    public PermissionDto createNewPermissionAssignToRole(PermissionDto permissionDto) {

        Permission map = modelMapper.map(permissionDto, Permission.class);

        if (permissionRepository.existsByName(permissionDto.getName())) {
            throw new ResourceAlreadyExistsException("Permission already exists with name: " + permissionDto.getName());
        }


        Permission save = permissionRepository.save(map);
        return modelMapper.map(save,PermissionDto.class);
    }

    //4. update permission

    @Transactional
    @Override
    public PermissionDto updatePermissionDetails(long permissionId, PermissionDto permissionDto) {
        Permission existingPermission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException("permission id is not present with this id: " + permissionId));

        if (!permissionDto.getName().equals(existingPermission.getName()) && permissionRepository.existsByName(permissionDto.getName())){
            throw new ResourceAlreadyExistsException("Permission already exists with name: " + permissionDto.getName());
        }
        existingPermission.setName(permissionDto.getName());
        existingPermission.setDescription(permissionDto.getDescription());


        Permission save = permissionRepository.save(existingPermission);
        return modelMapper.map(save,PermissionDto.class);
    }

    //5. delete permission
    @Override
    public void deletePermissionDetails(long permissionId) {

        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException("permission id is not present with this id: " + permissionId));
        permissionRepository.delete(permission);
    }
}
