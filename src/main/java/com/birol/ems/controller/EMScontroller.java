package com.birol.ems.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.birol.dto.VerifyCodeDto;
import com.birol.ems.dto.EMPLOYEE_BASIC;
import com.birol.ems.repo.EmployeeRepo;
import com.birol.ems.service.PageService;
import com.birol.mail.MailService;
import com.birol.model.Account;
import com.birol.repository.AccountRepository;

@Controller
public class EMScontroller {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private PageService ps;
	@Autowired
	private EmployeeRepo emprepo;
	@Autowired
	private MailService mailservice;

	@GetMapping("ems")
	public String verifyCode(Model model, VerifyCodeDto verifyCodeDto) {

		return "admin";
	}
	
	@GetMapping("getUsersbyRole")
	public String getUsersbyRole(@RequestParam String roleid, Model model, VerifyCodeDto verifyCodeDto) {
		ArrayList<EMPLOYEE_BASIC> users = new ArrayList<EMPLOYEE_BASIC>();
		users = (ArrayList<EMPLOYEE_BASIC>) emprepo.findbyrole(roleid);
		model.addAttribute("users", users);
		return "UI/ajaxGetemployeByroleOptions";
	}

	@GetMapping("getNavPage")
	public String getNavPage(@RequestParam String navid,Pageable pageable, Model model, HttpServletRequest request,Principal principal) {		
		String return_str=ps.getPageData(navid,pageable,model,request,principal);
		return return_str;
	}
	
	@GetMapping("calendar")
	public String calendar(Model model, Principal principal) {
		model.addAttribute("pageTitle","Calendar");					
		model.addAttribute("user", accountRepository.findByUsername(principal.getName()).get());
		return "pages/calendar";
	}
	
	@GetMapping("change_password_do")
	public String change_password_do(@RequestParam String email,
			@RequestParam String password,
			Principal principal,
			Model model, VerifyCodeDto verifyCodeDto) {
		
		Account user= accountRepository.findByUsername(principal.getName()).get();		
		if(email.equals(user.getEmail())) {
			Account ac= new Account();
			ac.setPassword(password);
			accountRepository.save(ac);
			model.addAttribute("message","Password Updated Successfully!");
		}else {
			PageService ps = new PageService();
			ps.PasswordChangeTimeline(principal);
			model.addAttribute("message","Email address is not registered");
		}
		
		return "redirect:/";
	}
	
	@GetMapping("addEmployeeDo")
	public String addEmployeeDo(@ModelAttribute EMPLOYEE_BASIC emp,Model model,Principal p) {
		try {
			Account acc = accountRepository.getOne(emp.getUserid());
			acc.setActive(emp.isStatus());
			accountRepository.save(acc);
			emprepo.save(emp);
			ps.SuccessTimeline(p,"addEmployeeDo","Information Added for: "+emp.getFirst_name()+" "+emp.getLast_name());
		}catch (Exception e) {
			ps.ErrorTimeline(p, "addEmployeeDo", e.getMessage());
		}
		
		return "redirect:/";	 
		
	}
	
	@RequestMapping(value = "/emp_profile_add_edit_do", method=RequestMethod.POST)
	public String emp_profile_edit(@ModelAttribute EMPLOYEE_BASIC emp,Model model, Principal p) {
		try {			
			EMPLOYEE_BASIC emp_exits= new EMPLOYEE_BASIC();
			if(emp.getEmpid()!=null) {
				
				emp_exits= emprepo.findbyEmpid(emp.getEmpid());
				emp.setEmpid(emp_exits.getEmpid());
				if(emp.getUserid()!=null) {
					Account acc = accountRepository.getOne(emp.getUserid());
					acc.setActive(emp.isStatus());
					accountRepository.save(acc);	
				}			
										
			}else {
				mailservice.sendEmailForNewUser(emp);
			}
			
			try {
				if(emp.getEmp_image_m().getSize()>0) {
					emp.setEmp_image(emp.getEmp_image_m().getBytes());
				}else {
					emp.setEmp_image(emp_exits.getEmp_image());
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			try {
				if(emp.getDoc_m_cv().getSize()>0) {
					emp.setDoc_cv(emp.getDoc_m_cv().getBytes());
				}else {
					emp.setDoc_cv(emp_exits.getDoc_cv());
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			try {
				if(emp.getDoc_m_crt().getSize()>0) {
					emp.setDoc_certificate(emp.getDoc_m_crt().getBytes());
				}else {
					emp.setDoc_certificate(emp_exits.getDoc_certificate());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			try {
				if(emp.getDoc_m_id().getSize()>0) {
					emp.setDoc_id(emp.getDoc_m_id().getBytes());
				}else {
					emp.setDoc_id(emp_exits.getDoc_id());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			emprepo.save(emp);
			//model.addAttribute("msg","Successfully updated info for "+emp.getFirst_name()+" "+emp.getLast_name());			
			String timeline_str=ps.SuccessTimeline(p,"emp_profile_add_edit_do","Information updated for: "+emp.getFirst_name()+" "+emp.getLast_name());			
			model.addAttribute("msg","Successfully updated info for "+emp.getFirst_name()+" "+emp.getLast_name()+"\n"+timeline_str);	
			return "UI/ajaxResponseTemplate";			
		}catch (Exception e) {
			e.printStackTrace();
			ps.ErrorTimeline(p, "emp_profile_add_edit_do", e.toString());
			//return "redirect:/";
			model.addAttribute("msg",e.toString());
			return "UI/ajaxResponseTemplate";
		}		
		
	}
	
	
	@GetMapping("/download/user")
    public void downloadUsersFile(@RequestParam String fileId,@RequestParam Long userid, HttpServletResponse resp,Principal principal) throws IOException {
  	
    	EMPLOYEE_BASIC emp= new EMPLOYEE_BASIC();		
		emp=emprepo.findbyUserid(userid);

    	byte[] byteArray = null;
    	String ext=".pdf";
    	if(fileId.equalsIgnoreCase("cv")) {
    		byteArray = emp.getDoc_cv();
    		//ext=Magic.getMagicMatch(bdata).getExtension();
        }else if(fileId.equalsIgnoreCase("crt")) {
    		byteArray = emp.getDoc_certificate();
        }else if(fileId.equalsIgnoreCase("id")) {        	
    		byteArray = emp.getDoc_id();
    		InputStream is = new ByteArrayInputStream(byteArray);
            String mimeType = URLConnection.guessContentTypeFromStream(is);  
            if(mimeType!=null) {
            	ext="."+mimeType.split("/")[1];
            }
            System.out.println(mimeType);
        }    	
    	
        resp.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM.getType()); 
        resp.setHeader("Content-Disposition", "attachment; filename=" +emp.getUsername()+fileId+ext);
        resp.setContentLength(byteArray.length);

        OutputStream os = resp.getOutputStream();
        
        try {
            os.write(byteArray, 0, byteArray.length);
        } finally {
            os.close();
        }

    }

}
