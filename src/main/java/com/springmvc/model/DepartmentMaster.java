package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DEPARTMENT_MASTER")
public class DepartmentMaster extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="DepartmentMaster_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(nullable = false, unique = true)
	private String deptCode;
	private String deptDecription;
	private String deptLocation;
	
	public DepartmentMaster() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DepartmentMaster(String deptCode, String deptDecription, String deptLocation) {
		super();
		this.deptCode = deptCode;
		this.deptDecription = deptDecription;
		this.deptLocation = deptLocation;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptDecription() {
		return deptDecription;
	}
	public void setDeptDecription(String deptDecription) {
		this.deptDecription = deptDecription;
	}
	public String getDeptLocation() {
		return deptLocation;
	}
	public void setDeptLocation(String deptLocation) {
		this.deptLocation = deptLocation;
	}
	
}
