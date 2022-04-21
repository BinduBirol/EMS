package com.birol.validator.account_verify;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.birol.dao.account.AccountDao;
import com.birol.dao.verify_account.VerifyAccountDao;
import com.birol.model.Account;
import com.birol.model.VerifyAccount;

public class VerifyCodeValidator implements ConstraintValidator<ValidVerifyCode, String>{

	@Autowired
	private VerifyAccountDao verifyAccountDao;
	
	@Override
	public boolean isValid(String token, ConstraintValidatorContext context) {
		
		if(token.isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Code not be empty")
					.addConstraintViolation();
			return false;
		} else if(!verifyAccountDao.findByToken(token).isPresent()) {
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Code verification not found")
					.addConstraintViolation();
			return false;
			
			
		} else {
			VerifyAccount verifyAccount = verifyAccountDao.findByToken(token).get();
			if(verifyAccount.isExpired()) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("Verification code has expired")
						.addConstraintViolation();
				return false;
			}
		}
		
		
		return true;
	}
}
