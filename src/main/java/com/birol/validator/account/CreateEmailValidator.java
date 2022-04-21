package com.birol.validator.account;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.birol.service.account.AccountService;

public class CreateEmailValidator implements ConstraintValidator<ValidCreateEmail, String>{

	@Autowired
	private AccountService accountService;
	
	@Override
	public void initialize(ValidCreateEmail constraintAnnotation) {
		// TODO Auto-generated method stub
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		
		if(accountService.findByEmail(email).isPresent()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Email already taken")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

}
