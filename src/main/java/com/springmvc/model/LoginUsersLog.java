package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "USERS_MASTER_log")
public class LoginUsersLog extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="LoginUsersLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn
	private LoginUsers loginUserOld;
	
	@Column(name = "USER_NAME",  nullable = false)
	private String username;
	@Column(name = "PASSWORD", nullable = false)
	private String password;	
	@Column(name = "FULL_NAME", nullable = false)
	private String fullName;
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn
	private RoleMaster role;
	@Column(name="ACCOUNT_NOT_LOCK")
	private boolean accountNotLock = true;
	@Column(name="ACCOUNT_NOT_DISABLE")
	private boolean accountNotDisable = true;
	@Column(name="ACCOUNT_EXPIRE_DATE")
	private Date accountexprie;
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn
	private StudyMaster activeStudy;
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn
	private StudySite activeSite;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String tranPassword;

	private String status = "Active";
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Transient
	private List<StudySite> siteList;
	
	public List<StudySite> getSiteList() {
		return siteList;
	}
	public void setSiteList(List<StudySite> siteList) {
		this.siteList = siteList;
	}
	public StudySite getActiveSite() {
		return activeSite;
	}
	public void setActiveSite(StudySite activeSite) {
		this.activeSite = activeSite;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public RoleMaster getRole() {
		return role;
	}
	public void setRole(RoleMaster role) {
		this.role = role;
	}
	public boolean isAccountNotLock() {
		return accountNotLock;
	}
	public void setAccountNotLock(boolean accountNotLock) {
		this.accountNotLock = accountNotLock;
	}
	public boolean isAccountNotDisable() {
		return accountNotDisable;
	}
	public void setAccountNotDisable(boolean accountNotDisable) {
		this.accountNotDisable = accountNotDisable;
	}
	public Date getAccountexprie() {
		return accountexprie;
	}
	public void setAccountexprie(Date accountexprie) {
		this.accountexprie = accountexprie;
	}
	public StudyMaster getActiveStudy() {
		return activeStudy;
	}
	public void setActiveStudy(StudyMaster activeStudy) {
		this.activeStudy = activeStudy;
	}
	public String getTranPassword() {
		return tranPassword;
	}
	public void setTranPassword(String tranPassword) {
		this.tranPassword = tranPassword;
	}
	public LoginUsers getLoginUserOld() {
		return loginUserOld;
	}
	public void setLoginUserOld(LoginUsers loginUserOld) {
		this.loginUserOld = loginUserOld;
	}
	
	
}
