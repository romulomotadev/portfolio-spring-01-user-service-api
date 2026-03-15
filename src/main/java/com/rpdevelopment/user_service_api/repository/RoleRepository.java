package com.rpdevelopment.user_service_api.repository;

import com.rpdevelopment.user_service_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
