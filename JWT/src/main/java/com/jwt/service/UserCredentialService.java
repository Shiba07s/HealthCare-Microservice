package com.jwt.service;


import com.jwt.dtos.UserDto;
import com.jwt.entity.UserCredential;
import com.jwt.request.UserCreateRequest;

import java.util.List;

public interface UserCredentialService {

//    UserDto createNewUser(UserDto userDto);

    UserDto createUser(UserCreateRequest createRequest);

    UserDto getUserById(long userId);

    List<UserDto> getAllUserList();

    UserDto updateUserDetails(long userId, UserDto userDto);

    void deleteUser(long userId);

    UserDto updateUserStatus(Long id, boolean active);

    UserDto addRoleToUser(Long userId, Long roleId);

    UserDto removeRoleFromUser(Long userId, Long roleId);

    void updateLastLoginDate(String username);


}
