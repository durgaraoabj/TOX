package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TESTCODE_PROFILE_PARAMETERS")
public class TestCodeProfileParameters extends CommonMaster   implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="TestCodeProfileParameters_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="testCodeProfileId")
	private TestCodeProfile testCodeProfile;
	@ManyToOne
	@JoinColumn(name="testCodeId")
	private TestCode testCode;
	private int orderNo = 0;
	
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TestCodeProfile getTestCodeProfile() {
		return testCodeProfile;
	}
	public void setTestCodeProfile(TestCodeProfile testCodeProfile) {
		this.testCodeProfile = testCodeProfile;
	}
	public TestCode getTestCode() {
		return testCode;
	}
	public void setTestCode(TestCode testCode) {
		this.testCode = testCode;
	}
	
}
