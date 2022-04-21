package com.birol.ems.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.birol.ems.dto.EMPLOYEE_BASIC;
import com.birol.ems.dto.SideMenuDTO;

public interface SideMenuRepo extends CrudRepository<SideMenuDTO, String> {	
	
	String getbyroleQ= "SELECT * FROM theme_side_menu WHERE role_id = ?1  and status='A' ";
	@Query(value =getbyroleQ,nativeQuery = true)
	ArrayList<SideMenuDTO> findbyRole(long roleid);

}
