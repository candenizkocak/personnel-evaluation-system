package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name); // Custom method to find a role by name
}