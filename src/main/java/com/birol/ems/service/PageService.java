package com.birol.ems.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import com.birol.dao.role.RoleDao;
import com.birol.ems.dto.EMPLOYEE_BASIC;
import com.birol.ems.dto.TIMELINE;
import com.birol.ems.repo.EmployeeRepo;
import com.birol.ems.repo.TimelineRepo;
import com.birol.ems.repo.TypeRepo;
import com.birol.model.Account;
import com.birol.repository.AccountRepository;

@Service
public class PageService {
	
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private EmployeeRepo emprepo;
	@Autowired
	private TimelineRepo timelineRepo;
	@Autowired
	private RoleDao roledao;
	@Autowired
	private TypeRepo typeRepo;

	public String getPageData(String navid,Pageable pageable, Model model, HttpServletRequest request,Principal principal) {
		try {
			model.addAttribute("page", accountRepository.findAll(pageable));
			model.addAttribute("users", accountRepository.findByUsername(principal.getName()).get());			
			setSelectOptionValues(model);
			setLoggedinUserInfo(model,accountRepository.findByUsername(principal.getName()).get());			
			
			EMPLOYEE_BASIC empObj = new EMPLOYEE_BASIC();
			Account usersumm= accountRepository.findByUsername(principal.getName()).get();
			empObj = emprepo.findbyUserid(usersumm.getId());
			
			String return_str="pages/"+navid;
			if(navid.equals("employee_profile")) {
				Long get_emp_id=Long.parseLong(request.getParameter("emp_id"));
				model.addAttribute("emp_profile", emprepo.findbyUserid(get_emp_id));
			}else if(navid.equals("employee_list")) {
				model.addAttribute("emp_list", getEmployeeList());
			}else if(navid.equals("employee_work_schedule")) {
				model.addAttribute("emp_list", getActiveEmployeeList());
			}else if(navid.equals("work_sh_modal")) {
				model.addAttribute("emp_wrk_sh_modal", getEmployeeList());
				return_str="pages/modal/work_sh_modal";
			}else if(navid.equals("emp_profile_edit")) {
				try {
					Long get_emp_id=Long.parseLong(request.getParameter("emp_id"));			
					EMPLOYEE_BASIC e=emprepo.findbyEmpid(get_emp_id);
					if(e.getEmp_image()!=null) {
						String imageencode = Base64.getEncoder().encodeToString(e.getEmp_image());
				    	e.setEmp_image_encoded(imageencode);			    	
					}
					model.addAttribute("emp_info", e);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
			}else if(navid.equals("add_employee")) {
				//model.addAttribute("roles",roledao.findAll());						
			}else if(navid.equals("timeline")) {
				ArrayList<TIMELINE> tl= new ArrayList<TIMELINE>();
				if(empObj.getRoleid()==1) {
					tl= timelineRepo.getAlldesc();
				}else {
					tl= timelineRepo.getbyUserdesc(empObj.getUsername());
				}			
				model.addAttribute("timeline",tl);						
			}else if(navid.equals("profile")) {			
				if(empObj.getEmp_image()!=null) {
					String imageencode = Base64.getEncoder().encodeToString(empObj.getEmp_image());
					empObj.setEmp_image_encoded(imageencode);			    	
				}		
				model.addAttribute("me",empObj);				
			}
			
			
			return return_str;
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("error",e.toString());			
			return "theme/error";
		}
		
	}
	
	


	private void setSelectOptionValues(Model model) {
		model.addAttribute("roles",roledao.findAll());
		model.addAttribute("type",typeRepo.findAll());
		EMPLOYEE_BASIC empObj = new EMPLOYEE_BASIC();		
	}
	
	public void setLoggedinUserInfo(Model model, Account usersumm) {
		try {
			EMPLOYEE_BASIC empObj = new EMPLOYEE_BASIC();
			//System.out.println("Logged in user: "+usersumm.getUsername());
			empObj=emprepo.findbyUserid(usersumm.getId());
			try {
				if(empObj.getEmp_image()!=null) {
					String imageencode = Base64.getEncoder().encodeToString(empObj.getEmp_image());
					empObj.setEmp_image_encoded(imageencode);			    	
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			//contract end info
			Period p = Period.between(LocalDate.now(),LocalDate.parse(empObj.getContract_end()));				
			String format_p=p.toString().replace("P", "").replace("Y", "Years ").replace("M", "Months ").replace("D", "Days");				
			empObj.setContact_status_str("align-middle");
			if(format_p.startsWith("-"))empObj.setContact_status_str("text-danger align-middle");
			empObj.setContact_remaining_period(format_p);
			
			//contract start info
			Period csp = Period.between(LocalDate.parse(empObj.getContract_start()),LocalDate.now());	
			String format_csp=csp.toString().replace("P", "").replace("Y", "Years ").replace("M", "Months ").replace("D", "Days");	
			empObj.setContact_started_period(format_csp);
			
			//contact %
			long p2 = ChronoUnit.DAYS.between(LocalDate.parse(empObj.getContract_start()),LocalDate.parse(empObj.getContract_end()));
			long ps2 = ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.parse(empObj.getContract_end()));
			long pe2 = ChronoUnit.DAYS.between(LocalDate.parse(empObj.getContract_start()),LocalDate.now());
			
			long lps2= (100*ps2)/p2;
			long lpe2= (100*pe2)/p2;
			
			empObj.setContract_start_persent(String.valueOf(lps2));
			empObj.setContract_end_persent(String.valueOf(lpe2));
			
			model.addAttribute("userdtl",empObj);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}


	public ArrayList<EMPLOYEE_BASIC> getEmployeeList() {
		ArrayList<EMPLOYEE_BASIC> eList_all = new ArrayList<EMPLOYEE_BASIC>();
		try {			
			eList_all= (ArrayList<EMPLOYEE_BASIC>) emprepo.findAll();
			for(EMPLOYEE_BASIC e: eList_all) {
				//setting up image
				if(e.getEmp_image()!=null) {
					String imageencode = Base64.getEncoder().encodeToString(e.getEmp_image());
			    	e.setEmp_image_encoded(imageencode);			    	
				}				
				
				Period p = Period.between(LocalDate.now(),LocalDate.parse(e.getContract_end()));				
				String format_p=p.toString().replace("P", "").replace("Y", "Years ").replace("M", "Months ").replace("D", "Days");				
				e.setContact_status_str("align-middle");
				if(format_p.startsWith("-"))e.setContact_status_str("text-danger align-middle");
				e.setContact_remaining_period(format_p);			
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();			
		}
		return eList_all;
	}
	
	public ArrayList<EMPLOYEE_BASIC> getActiveEmployeeList() {
		ArrayList<EMPLOYEE_BASIC> eList_all = new ArrayList<EMPLOYEE_BASIC>();
		try {			
			eList_all= (ArrayList<EMPLOYEE_BASIC>) emprepo.findbyStatus(1);
			for(EMPLOYEE_BASIC e: eList_all) {
				//setting up image
				if(e.getEmp_image()!=null) {
					String imageencode = Base64.getEncoder().encodeToString(e.getEmp_image());
			    	e.setEmp_image_encoded(imageencode);			    	
				}				
				
				Period p = Period.between(LocalDate.now(),LocalDate.parse(e.getContract_end()));				
				String format_p=p.toString().replace("P", "").replace("Y", "Years ").replace("M", "Months ").replace("D", "Days");				
				e.setContact_status_str("align-middle");
				if(format_p.startsWith("-"))e.setContact_status_str("text-danger align-middle");
				e.setContact_remaining_period(format_p);			
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();			
		}
		return eList_all;
	}
	
	
	public String PasswordChangeTimeline(Principal principal) {		
		TIMELINE tm = new TIMELINE();
		//tm= (TIMELINE) timelineRepo.findAll();
		tm.setActivity_title("Password changing attempt");
		//tm.setActivity_user_id(principal.getName()).get());
		tm.setActivity_username(principal.getName());
		tm.setActivity_desc("Email address is not registered");
		tm.setActivity_icon("fa-exclamation-circle");
		//tm.setActivity_link("");
		tm.setRole_id(1);
		tm.setType("private");
		//timelineRepo.save(tm);
		return "saved to timeline";
	}
	
	public String ErrorTimeline(Principal principal,String title, String msg) {		
		try {
			TIMELINE tm = new TIMELINE();
			//tm= (TIMELINE) timelineRepo.findAll();
			tm.setActivity_title("Error: "+title);
			//tm.setActivity_user_id(principal.getName()).get());
			tm.setActivity_username(principal.getName());
			tm.setActivity_desc(msg);
			tm.setActivity_icon("fa-ban text-danger");
			//tm.setActivity_link("");
			tm.setRole_id(1);
			tm.setType("private");
			timelineRepo.save(tm);
			return ".";
		}catch (Exception e) {
			//model.addAttribute("msg",e.toString());
			return e.getMessage();
		}		
		
	}
	
	public String SuccessTimeline(Principal principal,String title, String msg) {		
		try {
			TIMELINE tm = new TIMELINE();
			//tm= (TIMELINE) timelineRepo.findAll();
			tm.setActivity_title("Success: "+title);
			//int userid=principal.getName().get();
			//tm.setActivity_user_id(principal.getName()).get();
			tm.setActivity_username(principal.getName());
			tm.setActivity_desc(msg);
			tm.setActivity_icon("text-success fa-check");
			//tm.setActivity_link("");
			tm.setRole_id(1);
			tm.setType("admin");
			timelineRepo.save(tm);
			return ".";
		}catch (Exception e) {
			return e.getMessage();
		}
		
	}


	public void setDashboardinfo(Long roleid, Model model) {
		if(roleid==1) {
			model.addAttribute("r_user",accountRepository.findAll().size());			
			model.addAttribute("t_user",emprepo.findAll().spliterator().getExactSizeIfKnown());
			model.addAttribute("a_user",emprepo.findbyStatus(1).size());
			model.addAttribute("i_user",emprepo.findbyStatus(0).size());
		}
		
	}
	
}
