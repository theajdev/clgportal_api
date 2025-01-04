package com.aj.clgportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aj.clgportal.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<Admin> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
}
