package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name="tox_user")
public class ToxUser extends CommonMaster  implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ToxUser_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="user_name")
	private String userName;
	@Column(name="password")
	private String password;
	@ManyToOne
	@JoinColumn(name="empMaster_id")
	private EmployeeMaster empMaster;
	@ManyToOne
	@JoinColumn(name="loginUser_id")
	private LoginUsers loginUserId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public EmployeeMaster getEmpMaster() {
		return empMaster;
	}
	public void setEmpMaster(EmployeeMaster empMaster) {
		this.empMaster = empMaster;
	}
	public LoginUsers getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(LoginUsers loginUserId) {
		this.loginUserId = loginUserId;
	}
	
	
}
