package com.birol.ems.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "theme_side_menu")
public class SideMenuDTO {
	private int role_id;
	private String menu_text;
	@Id
	private String menu_id;
	private String icon_class;
	private char status;
	
	public int getRole_id() {
		return role_id;
	}	
	
	public String getIcon_class() {
		return icon_class;
	}

	public void setIcon_class(String icon_class) {
		this.icon_class = icon_class;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getMenu_text() {
		return menu_text;
	}
	public void setMenu_text(String menu_text) {
		this.menu_text = menu_text;
	}
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public SideMenuDTO() {
		super();
		// TODO Auto-generated constructor stub
	}	

}
