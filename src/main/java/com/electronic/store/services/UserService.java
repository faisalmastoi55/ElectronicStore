package com.electronic.store.services;

import com.electronic.store.entities.User;
import com.electronic.store.payloads.PageableResponse;
import com.electronic.store.dtos.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete
    void deleteUser(String userId);

    //get all users
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);

    //search user
    List<UserDto> searchUser(String keyword);

    //Find User By Email For Google Auth
    Optional<User> findUserByEmailOptional(String email);

    //other user specific features
}
