package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.PermissionDTO;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    PermissionDTO createPermission(PermissionDTO permissionDTO);
    Optional<PermissionDTO> getPermissionById(Integer id);
    List<PermissionDTO> getAllPermissions();
    PermissionDTO updatePermission(Integer id, PermissionDTO permissionDetailsDTO);
    void deletePermission(Integer id);
}