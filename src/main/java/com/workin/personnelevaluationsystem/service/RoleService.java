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
    Optional<RoleDTO> getRoleByName(String name); // <--- ADD THIS LINE
}