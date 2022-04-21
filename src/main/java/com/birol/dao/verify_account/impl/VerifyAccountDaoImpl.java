package com.birol.dao.verify_account.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.birol.dao.verify_account.VerifyAccountDao;
import com.birol.model.VerifyAccount;
import com.birol.repository.VerifyAccountRepository;

@Repository
public class VerifyAccountDaoImpl implements VerifyAccountDao{

	@Autowired
	private VerifyAccountRepository verifyAccountRepository;
	
	@Override
	public VerifyAccount create(VerifyAccount verifyAccount) {
		return verifyAccountRepository.save(verifyAccount);
	}

	@Override
	public Optional<VerifyAccount> findByToken(String token) {
		return verifyAccountRepository.findByToken(token);
	}

	@Override
	public Optional<VerifyAccount> findById(Long id) {
		return verifyAccountRepository.findById(id);
	}

}
