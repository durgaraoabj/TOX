package com.springmvc.model.dummy;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.springmvc.model.RoleMaster;

public class LoginFieldDummyForm {

	private long id;
	private long empMasterId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthDate;
	private String empId;
	private RoleMaster role;
	private boolean tranPasswordflag=false;
	private boolean loginCredentials = false;
	private boolean accountNotLock = false;
	private boolean accountNotDisable = false;
	private boolean resetPassword = false;
	private boolean resetTranPassword = false;
	private String tranPassword="";
	
	public long getEmpMasterId() {
		return empMasterId;
	}
	public void setEmpMasterId(long empMasterId) {
		this.empMasterId = empMasterId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public RoleMaster getRole() {
		return role;
	}
	public void setRole(RoleMaster role) {
		this.role = role;
	}
	public boolean isTranPasswordflag() {
		return tranPasswordflag;
	}
	public void setTranPasswordflag(boolean tranPasswordflag) {
		this.tranPasswordflag = tranPasswordflag;
	}
	public boolean isLoginCredentials() {
		return loginCredentials;
	}
	public void setLoginCredentials(boolean loginCredentials) {
		this.loginCredentials = loginCredentials;
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
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getTranPassword() {
		return tranPassword;
	}
	public void setTranPassword(String tranPassword) {
		this.tranPassword = tranPassword;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isResetPassword() {
		return resetPassword;
	}
	public void setResetPassword(boolean resetPassword) {
		this.resetPassword = resetPassword;
	}
	public boolean isResetTranPassword() {
		return resetTranPassword;
	}
	public void setResetTranPassword(boolean resetTranPassword) {
		this.resetTranPassword = resetTranPassword;
	}
	
}
