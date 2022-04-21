package com.birol.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
public class Role implements Serializable{

	private static final long serialVersionUID = 8374992679263373931L;

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JoinTable(name = "employee_basic", joinColumns = {
			@JoinColumn(name = "roleid", referencedColumnName = "roleid") }, inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Long id;
	
	@Column(name="name", unique=true, length=100)
	private String name;
	
	@Lob
	@Column(name="description")
	private String description;
	
	@ManyToMany(mappedBy="roles")
	private Set<Account> users = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
