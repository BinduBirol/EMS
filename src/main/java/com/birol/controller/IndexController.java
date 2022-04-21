package com.birol.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.birol.dao.role.RoleDao;
import com.birol.dto.AccountCreateDto;
import com.birol.dto.VerifyCodeDto;
import com.birol.ems.dto.EMPLOYEE_BASIC;
import com.birol.ems.dto.SideMenuDTO;
import com.birol.ems.dto.TIMELINE;
import com.birol.ems.repo.EmployeeRepo;
import com.birol.ems.repo.SideMenuRepo;
import com.birol.ems.service.PageService;
import com.birol.mail.Mail;
import com.birol.mail.MailService;
import com.birol.model.Account;
import com.birol.model.Role;
import com.birol.repository.AccountRepository;
import com.birol.repository.RoleRepository;
import com.birol.service.account.AccountService;
import com.birol.service.role.RoleService;

import groovy.io.FileType;

@Controller
@RequestMapping("/")
public class IndexController {

	
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AccountService accountService;
	@Autowired
	private EmployeeRepo emprepo;
	@Autowired
	private PageService ps;	
	@Autowired
	private SideMenuRepo menu_repo;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MailService mailservice;
	
	public ArrayList<SideMenuDTO> menu_items; 
	
	@GetMapping
	public String home(Model model, Pageable pageable,Principal principal) {
		model.addAttribute("page", accountRepository.findAll(pageable));
		Account usersumm= accountRepository.findByUsername(principal.getName()).get();
		model.addAttribute("user", usersumm);
		Set<Role> user_role= usersumm.getRoles();
		Role roleObj = new Role();
		for(Role r:user_role) {
			roleObj= r;
		}
		
		menu_items=menu_repo.findbyRole( roleObj.getId());		
		model.addAttribute("menu_items", menu_items);
		
		ps.setLoggedinUserInfo(model,usersumm);	
		ps.setDashboardinfo(roleObj.getId(),model);
		return "layout";
	}	
	
	
	@GetMapping("/pagination")
	@ResponseBody
	public Page<Account> findAll(Pageable pageable) {
		return accountRepository.findAll(pageable);
	}
	
	@GetMapping("login")
	public String login(Model model, HttpServletRequest request) {
		return "login";
	}	
	
	
	
	
	
}
