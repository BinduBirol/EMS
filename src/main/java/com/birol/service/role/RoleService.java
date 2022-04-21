package com.birol.service.role;

import java.util.Optional;

import com.birol.model.Role;

public interface RoleService {

	Optional<Role> findById(Long id);
	Role create(Role role);
}
