package com.backwoodslabs.backwoods_crm_api.repository;

import com.backwoodslabs.backwoods_crm_api.model.ERole;
import com.backwoodslabs.backwoods_crm_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
