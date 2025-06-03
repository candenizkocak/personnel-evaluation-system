package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.RoleDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Permission;
import com.workin.personnelevaluationsystem.model.Role;
import com.workin.personnelevaluationsystem.repository.PermissionRepository;
import com.workin.personnelevaluationsystem.repository.RoleRepository;
import com.workin.personnelevaluationsystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For transactional operations

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository; // Inject PermissionRepository

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    private RoleDTO convertToDto(Role role) {
        if (role == null) return null;
        return RoleDTO.builder()
                .roleID(role.getRoleID())
                .name(role.getName())
                .description(role.getDescription())
                // Convert associated permissions to their IDs for the DTO response
                .permissionIDs(role.getPermissions().stream()
                        .map(Permission::getPermissionID)
                        .collect(Collectors.toSet()))
                .build();
    }

    // Helper to convert DTO to Entity, handling permission relationships
    private Role convertToEntity(RoleDTO roleDTO) {
        if (roleDTO == null) return null;
        Role role = Role.builder()
                .roleID(roleDTO.getRoleID()) // ID might be null for creation
                .name(roleDTO.getName())
                .description(roleDTO.getDescription())
                .build();

        if (roleDTO.getPermissionIDs() != null && !roleDTO.getPermissionIDs().isEmpty()) {
            Set<Permission> permissions = roleDTO.getPermissionIDs().stream()
                    .map(permId -> permissionRepository.findById(permId)
                            .orElseThrow(() -> new BadRequestException("Permission not found with ID: " + permId)))
                    .collect(Collectors.toSet());
            role.setPermissions(permissions);
        } else {
            role.setPermissions(new HashSet<>()); // Ensure it's not null
        }
        return role;
    }


    @Override
    @Transactional // Ensure all operations within this method are part of a single transaction
    public RoleDTO createRole(RoleDTO roleDTO) {
        // Check for duplicate role name before creating
        if (roleRepository.findByName(roleDTO.getName()).isPresent()) {
            throw new BadRequestException("Role with name '" + roleDTO.getName() + "' already exists.");
        }
        Role role = convertToEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return convertToDto(savedRole);
    }

    @Override
    public Optional<RoleDTO> getRoleById(Integer id) {
        return roleRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Integer id, RoleDTO roleDetailsDTO) {
        Role roleToUpdate = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));

        // Check for duplicate name if name is changed and belongs to another role
        if (!roleToUpdate.getName().equals(roleDetailsDTO.getName())) {
            roleRepository.findByName(roleDetailsDTO.getName())
                    .ifPresent(r -> {
                        if (!r.getRoleID().equals(id)) { // If existing role with this name has a different ID
                            throw new BadRequestException("Role with name '" + roleDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        roleToUpdate.setName(roleDetailsDTO.getName());
        roleToUpdate.setDescription(roleDetailsDTO.getDescription());

        // Update permissions: Fetch existing permissions and new ones, then update the set
        if (roleDetailsDTO.getPermissionIDs() != null) {
            Set<Permission> newPermissions = roleDetailsDTO.getPermissionIDs().stream()
                    .map(permId -> permissionRepository.findById(permId)
                            .orElseThrow(() -> new BadRequestException("Permission not found with ID: " + permId)))
                    .collect(Collectors.toSet());
            roleToUpdate.setPermissions(newPermissions); // This will manage the join table
        } else {
            roleToUpdate.setPermissions(new HashSet<>()); // Clear permissions if none provided in DTO
        }


        Role updatedRole = roleRepository.save(roleToUpdate);
        return convertToDto(updatedRole);
    }

    @Override
    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with ID: " + id);
        }
        // Note: If a role is referenced by a User, deleting it will throw a DataIntegrityViolationException.
        // You might want to handle this explicitly, e.g., by disassociating users first,
        // or by checking for associated users before deleting.
        roleRepository.deleteById(id);
    }
}