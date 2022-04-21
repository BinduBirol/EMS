package com.birol.ems.repo;

import org.springframework.data.repository.CrudRepository;

import com.birol.ems.dto.mst_emp_type;

public interface TypeRepo extends CrudRepository<mst_emp_type, String> {

}
