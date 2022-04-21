package com.birol.ems.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class mst_emp_type {
@Id	
private int typeid;
private String type_name;	
private String type_desc;	
private String style_class;
public int getTypeid() {
	return typeid;
}
public void setTypeid(int typeid) {
	this.typeid = typeid;
}
public String getType_name() {
	return type_name;
}
public void setType_name(String type_name) {
	this.type_name = type_name;
}
public String getType_desc() {
	return type_desc;
}
public void setType_desc(String type_desc) {
	this.type_desc = type_desc;
}
public String getStyle_class() {
	return style_class;
}
public void setStyle_class(String style_class) {
	this.style_class = style_class;
}
}
