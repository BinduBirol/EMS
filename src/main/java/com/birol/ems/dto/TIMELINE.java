package com.birol.ems.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIMELINE")
public class TIMELINE {
	@Id
	int timeline_id;
	int role_id;
	String activity_username;
	String activity_title;	
	String activity_desc;	
	String activity_link;	
	String activity_icon;
	@Column(name = "activity_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	String activity_date;	
	int activity_user_id;
	String type;
	
	public int getTimeline_id() {
		return timeline_id;
	}
	public void setTimeline_id(int timeline_id) {
		this.timeline_id = timeline_id;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getActivity_username() {
		return activity_username;
	}
	public void setActivity_username(String activity_username) {
		this.activity_username = activity_username;
	}
	public String getActivity_title() {
		return activity_title;
	}
	public void setActivity_title(String activity_title) {
		this.activity_title = activity_title;
	}
	public String getActivity_desc() {
		return activity_desc;
	}
	public void setActivity_desc(String activity_desc) {
		this.activity_desc = activity_desc;
	}
	public String getActivity_link() {
		return activity_link;
	}
	public void setActivity_link(String activity_link) {
		this.activity_link = activity_link;
	}
	public String getActivity_icon() {
		return activity_icon;
	}
	public void setActivity_icon(String activity_icon) {
		this.activity_icon = activity_icon;
	}
	public String getActivity_date() {
		return activity_date;
	}
	public void setActivity_date(String activity_date) {
		this.activity_date = activity_date;
	}
	public int getActivity_user_id() {
		return activity_user_id;
	}
	public void setActivity_user_id(int activity_user_id) {
		this.activity_user_id = activity_user_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
	
}
