package com.birol.service.account_verify;

import java.util.Optional;

import com.birol.model.VerifyAccount;

public interface VerifyAccountService {

	VerifyAccount create(VerifyAccount verifyAccount);
	Optional<VerifyAccount> findByToken(String token);
	Optional<VerifyAccount> findById(Long id);
	
}
