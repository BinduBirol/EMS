package com.birol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.birol.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{}