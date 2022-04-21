package com.birol.ems.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Employee_work_schedule_dtl {


	private String work_sh_id; 
	@Id
	private String date; 	
	private String insert_time;
	
	
	
	public String getWork_sh_id() {
		return work_sh_id;
	}
	public void setWork_sh_id(String work_sh_id) {
		this.work_sh_id = work_sh_id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getInsert_time() {
		return insert_time;
	}
	public void setInsert_time(String insert_time) {
		this.insert_time = insert_time;
	} 
	
	
}
