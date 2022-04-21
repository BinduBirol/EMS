package com.birol.ems.repo;

import org.springframework.data.repository.CrudRepository;

import com.birol.ems.dto.USER_LOGIN;

public interface loginRepo extends CrudRepository<USER_LOGIN, String> {

}
