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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

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
                .permissionIDs(role.getPermissions().stream()
                        .map(Permission::getPermissionID)
                        .collect(Collectors.toSet()))
                .build();
    }

    private Role convertToEntity(RoleDTO roleDTO) {
        if (roleDTO == null) return null;
        Role role = Role.builder()
                .roleID(roleDTO.getRoleID())
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
            role.setPermissions(new HashSet<>());
        }
        return role;
    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
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

        if (!roleToUpdate.getName().equals(roleDetailsDTO.getName())) {
            roleRepository.findByName(roleDetailsDTO.getName())
                    .ifPresent(r -> {
                        if (!r.getRoleID().equals(id)) {
                            throw new BadRequestException("Role with name '" + roleDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        roleToUpdate.setName(roleDetailsDTO.getName());
        roleToUpdate.setDescription(roleDetailsDTO.getDescription());

        if (roleDetailsDTO.getPermissionIDs() != null) {
            Set<Permission> newPermissions = roleDetailsDTO.getPermissionIDs().stream()
                    .map(permId -> permissionRepository.findById(permId)
                            .orElseThrow(() -> new BadRequestException("Permission not found with ID: " + permId)))
                    .collect(Collectors.toSet());
            roleToUpdate.setPermissions(newPermissions);
        } else {
            roleToUpdate.setPermissions(new HashSet<>());
        }

        Role updatedRole = roleRepository.save(roleToUpdate);
        return convertToDto(updatedRole);
    }

    @Override
    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with ID: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override // <--- ADD THIS METHOD
    public Optional<RoleDTO> getRoleByName(String name) {
        return roleRepository.findByName(name).map(this::convertToDto);
    }
}