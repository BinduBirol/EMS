package com.birol.dto;

import com.birol.validator.account_verify.ValidVerifyCode;

public class VerifyCodeDto {

	@ValidVerifyCode
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
