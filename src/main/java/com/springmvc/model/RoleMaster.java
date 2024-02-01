package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name="ROLE_MASTER")
public class RoleMaster extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="RoleMaster_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(unique=true, nullable=false)
	private String role;
	private String roleDesc;
	private Character status='T';
	private boolean tranPassword = false;
	
	
	public RoleMaster() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RoleMaster(String role, String roleDesc, Character status, boolean tranPassword) {
		super();
		this.role = role;
		this.roleDesc = roleDesc;
		this.status = status;
		this.tranPassword = tranPassword;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public boolean isTranPassword() {
		return tranPassword;
	}
	public void setTranPassword(boolean tranPassword) {
		this.tranPassword = tranPassword;
	}
	
	
	

}
