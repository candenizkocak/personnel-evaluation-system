package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.RoleDTO;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    Optional<RoleDTO> getRoleById(Integer id);
    List<RoleDTO> getAllRoles();
    RoleDTO updateRole(Integer id, RoleDTO roleDetailsDTO);
    void deleteRole(Integer id);
    // You might add methods to manage permissions on a role later
    // RoleDTO addPermissionToRole(Integer roleId, Integer permissionId);
    // RoleDTO removePermissionFromRole(Integer roleId, Integer permissionId);
}