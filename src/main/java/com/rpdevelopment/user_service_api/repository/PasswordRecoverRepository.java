package com.rpdevelopment.user_service_api.repository;

import com.rpdevelopment.user_service_api.entity.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {
}
