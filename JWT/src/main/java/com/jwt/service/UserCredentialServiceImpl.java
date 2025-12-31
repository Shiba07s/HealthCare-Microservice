package com.jwt.service;

import com.jwt.dtos.UserDto;
import com.jwt.entity.Role;
import com.jwt.entity.UserCredential;
import com.jwt.exception.ResourceAlreadyExistsException;
import com.jwt.exception.ResourceNotFoundException;
import com.jwt.repository.RoleRepository;
import com.jwt.repository.UserCredentialRepository;
import com.jwt.request.UserCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest createRequest) {
        // Validate username and email
        if (userCredentialRepository.existsByUsername(createRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists: " + createRequest.getUsername());
        }

        if (userCredentialRepository.existsByEmail(createRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists: " + createRequest.getEmail());
        }

        // Create new user
        UserCredential user = new UserCredential();
        user.setUsername(createRequest.getUsername());
        user.setPassword(createRequest.getPassword());
        user.setEmail(createRequest.getEmail());
        user.setActive(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setCreatedAt(LocalDateTime.now());

        // Assign roles
        Set<Role> roles = new HashSet<>();
        if (createRequest.getRoleNames() != null && !createRequest.getRoleNames().isEmpty()) {
            for (String roleName : createRequest.getRoleNames()) {
                Role role = roleRepository.findByName(roleName);
                if (role == null) {
                    throw new ResourceNotFoundException("Role not found with name: " + roleName);
                }
                roles.add(role);
            }
        } else {
            // Assign default role if none specified
            Role defaultRole = roleRepository.findByName("USER");
            if (defaultRole == null) {
                throw new ResourceNotFoundException("Default role 'USER' not found");
            }
            roles.add(defaultRole);
        }

        user.setRoles(roles);
        user = userCredentialRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }


//    @Override
//    public UserDto createNewUser(UserDto userDto) {
//        UserCredential userCredential = modelMapper.map(userDto, UserCredential.class);
//
//        Set<Role> roleSet = userDto.getRoles().stream()
//                .map(r -> {
//                    Role role = roleRepository.findByName(r.getName());
//                    if (role == null) {
//                        throw new ResourceNotFoundException("Role not found: " + r.getName());
//                    }
//                    return role;
//                })
//                .collect(Collectors.toSet());
//
//        userCredential.setRoles(roleSet);
//        UserCredential savedUser = userCredentialRepository.save(userCredential);
//        return modelMapper.map(savedUser, UserDto.class);
//    }

    @Override
    public UserDto getUserById(long userId) {
        UserCredential user = userCredentialRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUserList() {
        List<UserCredential> users = userCredentialRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto updateUserStatus(Long id, boolean active) {
        UserCredential user = userCredentialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setActive(active);
        user = userCredentialRepository.save(user);

        return modelMapper.map(user,UserDto.class);
    }

    @Transactional
    @Override
    public UserDto addRoleToUser(Long userId, Long roleId) {
        UserCredential user = userCredentialRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        user.getRoles().add(role);
        user = userCredentialRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto removeRoleFromUser(Long userId, Long roleId) {
        UserCredential user = userCredentialRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        if (!user.getRoles().contains(role)) {
            throw new ResourceNotFoundException("Role with id: " + roleId + " is not assigned to the User");
        }
        user.getRoles().remove(role);
        user = userCredentialRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }
    @Transactional
    @Override
    public void updateLastLoginDate(String username) {
        UserCredential user = userCredentialRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        user.setLastLoginDate(LocalDateTime.now());
        userCredentialRepository.save(user);
    }

    @Override
     public UserDto updateUserDetails(long userId, UserDto userDto) {
        UserCredential existingUser = userCredentialRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setActive(userDto.isActive());

        Set<Role> roleSet = userDto.getRoles().stream()
                .map(r -> {
                    Role role = roleRepository.findByName(r.getName());
                    if (role == null) {
                        throw new ResourceNotFoundException("Role not found: " + r.getName());
                    }
                    return role;
                })
                .collect(Collectors.toSet());

        existingUser.setRoles(roleSet);
        UserCredential updated = userCredentialRepository.save(existingUser);
        return modelMapper.map(updated, UserDto.class);
    }

    @Override
    public void deleteUser(long userId) {
        UserCredential user = userCredentialRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userCredentialRepository.delete(user);
    }
}
