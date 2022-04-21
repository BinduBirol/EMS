package com.birol.dao.account;

import java.util.Optional;

import com.birol.dao.IOperations;
import com.birol.model.Account;

public interface AccountDao extends IOperations<Account> {

	Optional<Account> findByUsernameOrEmail(String username, String email);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByUsername(String username);
	
}