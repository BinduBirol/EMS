package com.birol.service.account;

import java.util.Optional;

import com.birol.dto.AccountCreateDto;
import com.birol.dto.VerifyCodeDto;
import com.birol.model.Account;

public interface AccountService {

	public Account createMember(AccountCreateDto accountDto) throws Exception;
	
	public Account createAdmin(AccountCreateDto accountDto);
	
	Optional<Account> findByUsernameOrEmail(String username, String email);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByUsername(String username);
	
	Optional<Account> findById(Long id);
	
	public void verifyCode(VerifyCodeDto verifyCodeDto);
}
