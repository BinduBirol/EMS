package com.birol.dao.verify_account;

import java.util.Optional;

import com.birol.model.VerifyAccount;

public interface VerifyAccountDao {

	VerifyAccount create(VerifyAccount verifyAccount);
	Optional<VerifyAccount> findByToken(String token);
	Optional<VerifyAccount> findById(Long id);
}
