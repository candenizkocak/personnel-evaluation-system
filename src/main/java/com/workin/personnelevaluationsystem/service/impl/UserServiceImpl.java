package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.UserCreateDTO;
import com.workin.personnelevaluationsystem.dto.UserResponseDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Employee;
import com.workin.personnelevaluationsystem.model.Role;
import com.workin.personnelevaluationsystem.model.User;
import com.workin.personnelevaluationsystem.repository.EmployeeRepository;
import com.workin.personnelevaluationsystem.repository.RoleRepository;
import com.workin.personnelevaluationsystem.repository.UserRepository;
import com.workin.personnelevaluationsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           EmployeeRepository employeeRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // Helper to convert Entity to Response DTO
    private UserResponseDTO convertToResponseDto(User user) {
        if (user == null) return null;
        return UserResponseDTO.builder()
                .userID(user.getUserID())
                .username(user.getUsername())
                .employeeID(user.getEmployee() != null ? user.getEmployee().getEmployeeID() : null)
                .employeeName(user.getEmployee() != null ?
                        user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName() : null)
                .lastLogin(user.getLastLogin())
                .isLocked(user.getIsLocked())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }

    // Helper to convert Create DTO to Entity (for internal use)
    // This will hash the password and link relationships
    private User convertToEntity(UserCreateDTO userCreateDTO) {
        if (userCreateDTO == null) return null;

        // Check for duplicate username for new user or if username is changed
        if (userCreateDTO.getUserID() == null || !userRepository.findById(userCreateDTO.getUserID())
                .map(User::getUsername)
                .orElse("") // Use empty string for null safety
                .equals(userCreateDTO.getUsername())) {
            if (userRepository.findByUsername(userCreateDTO.getUsername()).isPresent()) {
                throw new BadRequestException("Username '" + userCreateDTO.getUsername() + "' already exists.");
            }
        }

        // Check if employee already has a user account (only if a new employeeID is being linked or changed)
        if (userCreateDTO.getEmployeeID() != null) {
            userRepository.findByEmployee_EmployeeID(userCreateDTO.getEmployeeID())
                    .ifPresent(u -> {
                        // Allow update if it's the same user being updated
                        if (userCreateDTO.getUserID() == null || !u.getUserID().equals(userCreateDTO.getUserID())) {
                            throw new BadRequestException("Employee with ID " + userCreateDTO.getEmployeeID() + " already has an associated user account (User ID: " + u.getUserID() + ").");
                        }
                    });
        }

        User user = User.builder()
                .userID(userCreateDTO.getUserID())
                .username(userCreateDTO.getUsername())
                .passwordHash(bCryptPasswordEncoder.encode(userCreateDTO.getPassword())) // Hash the password
                .lastLogin(null) // Not set on creation/update via DTO, set by system on login
                .isLocked(userCreateDTO.getIsLocked())
                .build();

        // Set Employee relationship
        if (userCreateDTO.getEmployeeID() != null) {
            Employee employee = employeeRepository.findById(userCreateDTO.getEmployeeID())
                    .orElseThrow(() -> new BadRequestException("Employee not found with ID: " + userCreateDTO.getEmployeeID()));
            user.setEmployee(employee);
        } else {
            user.setEmployee(null); // Allow user without associated employee if business logic permits
        }

        // Set Roles relationship
        if (userCreateDTO.getRoleIDs() != null && !userCreateDTO.getRoleIDs().isEmpty()) {
            Set<Role> roles = userCreateDTO.getRoleIDs().stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new BadRequestException("Role not found with ID: " + roleId)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>()); // Ensure roles set is not null if no roles provided
        }
        return user;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        User user = convertToEntity(userCreateDTO); // Handles validations for employee/username
        User savedUser = userRepository.save(user);
        return convertToResponseDto(savedUser);
    }

    @Override
    public Optional<UserResponseDTO> getUserById(Integer id) {
        return userRepository.findById(id).map(this::convertToResponseDto);
    }

    @Override
    public Optional<UserResponseDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::convertToResponseDto);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Integer id, UserCreateDTO userUpdateDTO) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Update username if provided and changed
        if (userUpdateDTO.getUsername() != null && !userToUpdate.getUsername().equals(userUpdateDTO.getUsername())) {
            if (userRepository.findByUsername(userUpdateDTO.getUsername()).isPresent()) {
                throw new BadRequestException("Username '" + userUpdateDTO.getUsername() + "' already exists.");
            }
            userToUpdate.setUsername(userUpdateDTO.getUsername());
        }

        // Update password only if a new password is provided in DTO
        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            userToUpdate.setPasswordHash(bCryptPasswordEncoder.encode(userUpdateDTO.getPassword()));
        }

        userToUpdate.setIsLocked(userUpdateDTO.getIsLocked() != null ? userUpdateDTO.getIsLocked() : userToUpdate.getIsLocked()); // Update isLocked if provided

        // Update Employee relationship
        if (userUpdateDTO.getEmployeeID() != null) {
            // Check if the employee is already linked to a different user
            userRepository.findByEmployee_EmployeeID(userUpdateDTO.getEmployeeID())
                    .ifPresent(u -> {
                        if (!u.getUserID().equals(id)) { // If existing user with this employee has a different ID
                            throw new BadRequestException("Employee with ID " + userUpdateDTO.getEmployeeID() + " is already associated with another user (User ID: " + u.getUserID() + ").");
                        }
                    });

            Employee newEmployee = employeeRepository.findById(userUpdateDTO.getEmployeeID())
                    .orElseThrow(() -> new BadRequestException("Employee not found with ID: " + userUpdateDTO.getEmployeeID()));
            userToUpdate.setEmployee(newEmployee);
        } else {
            userToUpdate.setEmployee(null); // Clear association if ID is null
        }

        // Update Roles relationship
        if (userUpdateDTO.getRoleIDs() != null) { // If roleIDs is null, it means no change, if empty, it means remove all
            Set<Role> newRoles = userUpdateDTO.getRoleIDs().stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new BadRequestException("Role not found with ID: " + roleId)))
                    .collect(Collectors.toSet());
            userToUpdate.setRoles(newRoles);
        } else {
            // If userUpdateDTO.getRoleIDs() is null, keep existing roles.
            // If you want to allow clearing roles by sending an empty set, ensure logic handles it.
            // Current logic for new roles already creates new HashSet if DTO list is empty.
        }

        User updatedUser = userRepository.save(userToUpdate);
        return convertToResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        // IMPORTANT: Consider cascading deletes or checks here.
        // If a user is referenced by anything else (e.g., PerformanceReviews.EvaluatorID, Feedback.SenderID/ReceiverID, EmployeeCompetencies.AssessedBy),
        // deleting them directly will cause a DataIntegrityViolationException.
        // For a robust system, you'd implement logic to:
        // 1. Soft-delete (set them inactive) instead of deleting.
        // 2. Nullify references or handle dependent records.
        userRepository.deleteById(id);
    }
    @Override
    @Transactional
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BadRequestException("The old password you entered is incorrect.");
        }

        user.setPasswordHash(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}