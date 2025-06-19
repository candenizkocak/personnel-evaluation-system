package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.UserCreateDTO;
import com.workin.personnelevaluationsystem.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO createUser(UserCreateDTO userCreateDTO);
    Optional<UserResponseDTO> getUserById(Integer id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Integer id, UserCreateDTO userUpdateDTO);
    void deleteUser(Integer id);
    Optional<UserResponseDTO> getUserByUsername(String username);
    void changePassword(Integer userId, String oldPassword, String newPassword); // ADD THIS
}