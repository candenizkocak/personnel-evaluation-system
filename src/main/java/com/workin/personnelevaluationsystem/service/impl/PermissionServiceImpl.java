package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.PermissionDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Permission;
import com.workin.personnelevaluationsystem.repository.PermissionRepository;
import com.workin.personnelevaluationsystem.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    private PermissionDTO convertToDto(Permission permission) {
        if (permission == null) return null;
        return PermissionDTO.builder()
                .permissionID(permission.getPermissionID())
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
    }

    private Permission convertToEntity(PermissionDTO permissionDTO) {
        if (permissionDTO == null) return null;
        return Permission.builder()
                .permissionID(permissionDTO.getPermissionID())
                .name(permissionDTO.getName())
                .description(permissionDTO.getDescription())
                .build();
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        if (permissionRepository.findByName(permissionDTO.getName()).isPresent()) {
            throw new BadRequestException("Permission with name '" + permissionDTO.getName() + "' already exists.");
        }
        Permission permission = convertToEntity(permissionDTO);
        Permission savedPermission = permissionRepository.save(permission);
        return convertToDto(savedPermission);
    }

    @Override
    public Optional<PermissionDTO> getPermissionById(Integer id) {
        return permissionRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionDTO updatePermission(Integer id, PermissionDTO permissionDetailsDTO) {
        Permission permissionToUpdate = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + id));

        if (!permissionToUpdate.getName().equals(permissionDetailsDTO.getName())) {
            permissionRepository.findByName(permissionDetailsDTO.getName())
                    .ifPresent(p -> {
                        if (!p.getPermissionID().equals(id)) {
                            throw new BadRequestException("Permission with name '" + permissionDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        permissionToUpdate.setName(permissionDetailsDTO.getName());
        permissionToUpdate.setDescription(permissionDetailsDTO.getDescription());

        Permission updatedPermission = permissionRepository.save(permissionToUpdate);
        return convertToDto(updatedPermission);
    }

    @Override
    public void deletePermission(Integer id) {
        if (!permissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Permission not found with ID: " + id);
        }
        // Note: If a permission is referenced by a Role, deleting it will throw a DataIntegrityViolationException.
        permissionRepository.deleteById(id);
    }
}