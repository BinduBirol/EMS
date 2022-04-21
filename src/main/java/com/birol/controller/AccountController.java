package com.birol.controller;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.birol.dto.AccountCreateDto;
import com.birol.dto.VerifyCodeDto;
import com.birol.ems.dto.EMPLOYEE_BASIC;
import com.birol.ems.repo.EmployeeRepo;
import com.birol.model.Account;
import com.birol.model.Role;
import com.birol.service.account.AccountService;

@Controller
public class AccountController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private EmployeeRepo emprepo;

	@GetMapping("sign-up")
	public String signUp(AccountCreateDto accountCreateDto, Model model) {
		return "sign-up";
	}

	@PostMapping("sign-up")
	public String signUp(@Valid AccountCreateDto accountCreateDto, BindingResult result) throws Exception {

		if (result.hasErrors()) {
			return "sign-up";
		}
		EMPLOYEE_BASIC emp= new EMPLOYEE_BASIC();
		emp= emprepo.findbyWorkMail(accountCreateDto.getEmail());
		if(emp!=null) {
			//accountCreateDto.setId(emp.getUserid());
			emp.setUsername(accountCreateDto.getUsername());
			Account account = accountService.createMember(accountCreateDto);
			//Role role= new Role();
			//role.setId(Long.valueOf((emp.getRoleid())));
			//account.setRoles((Set<Role>) role);
			emp.setUserid(account.getId());
			emprepo.save(emp);
			//accountCreateDto.setId(account.getId());
			
		}

		

		return "redirect:/verify-code";
	}

	@GetMapping("verify-code")
	public String verifyCode(Model model, VerifyCodeDto verifyCodeDto) {
		
		return "verify-code";
	}

	@PostMapping("verify-code")
	public String verifyCodeAction(Model model, @Valid VerifyCodeDto verifyCodeDto, BindingResult result) {
		if(result.hasErrors()) {
			return "verify-code";
		}
		
		accountService.verifyCode(verifyCodeDto);
		
		return "redirect:/login";
	}
}
