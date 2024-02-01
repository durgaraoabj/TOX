package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "EMPLOYEE_MASTER_log")
public class EmployeeMasterLog extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="EmployeeMasterLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="employeeMaster")
	private EmployeeMaster employeeMaster;
	@Column(unique = true, nullable = false)
	private String empId;
	@Column(nullable = false)
	private String firstName;
	private String middleName;
	@Column(nullable = false)
	private String lastName;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthDate;
	private String gender;
	private String maritalStatus;
	private String mobileNo;
	private String altMobileNo;
	private String phoneNo;
	private String emailId;
	private String faxNo;
	@ManyToOne
	@JoinColumn(name="role_id")
	private RoleMaster role;
	@ManyToOne
	@JoinColumn
	private AddressMasterLog addressMaster;
	private boolean loginCredantials = false;
	@ManyToOne
	@JoinColumn
	private EmpJobMasterLog jobMaster;
	@ManyToOne
	@JoinColumn
	private EmergencyContMasterLog emerContMaster;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
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
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getAltMobileNo() {
		return altMobileNo;
	}
	public void setAltMobileNo(String altMobileNo) {
		this.altMobileNo = altMobileNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public EmployeeMaster getEmployeeMaster() {
		return employeeMaster;
	}
	public void setEmployeeMaster(EmployeeMaster employeeMaster) {
		this.employeeMaster = employeeMaster;
	}
	public AddressMasterLog getAddressMaster() {
		return addressMaster;
	}
	public void setAddressMaster(AddressMasterLog addressMaster) {
		this.addressMaster = addressMaster;
	}
	public boolean isLoginCredantials() {
		return loginCredantials;
	}
	public void setLoginCredantials(boolean loginCredantials) {
		this.loginCredantials = loginCredantials;
	}
	public EmpJobMasterLog getJobMaster() {
		return jobMaster;
	}
	public void setJobMaster(EmpJobMasterLog jobMaster) {
		this.jobMaster = jobMaster;
	}
	public EmergencyContMasterLog getEmerContMaster() {
		return emerContMaster;
	}
	public void setEmerContMaster(EmergencyContMasterLog emerContMaster) {
		this.emerContMaster = emerContMaster;
	}
	public RoleMaster getRole() {
		return role;
	}
	public void setRole(RoleMaster role) {
		this.role = role;
	}
	
}