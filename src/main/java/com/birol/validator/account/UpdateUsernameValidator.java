package com.birol.validator.account;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.birol.dto.AccountUpdateDto;
import com.birol.model.Account;
import com.birol.service.account.AccountService;

public class UpdateUsernameValidator implements ConstraintValidator<ValidUpdateUsername, Object>{

	@Autowired
	private AccountService accountService;
	
	@Override
	public void initialize(ValidUpdateUsername constraintAnnotation) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		
		if(obj instanceof AccountUpdateDto) {
			AccountUpdateDto dto = (AccountUpdateDto) obj;
			Long id = dto.getId();
			String username = dto.getUsername();
			if(accountService.findById(id).isPresent()) {
				Account account = accountService.findById(id).get();
				if(!account.getUsername().equals(username)) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("Username already taken")
							.addPropertyNode("username").addConstraintViolation();
					return false;
				}
			}
			
		}
		
		return true;
	}

}
