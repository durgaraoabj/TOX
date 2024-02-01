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
@Table(name="TEST_CODE_LOG")
public class TestCodeLog extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="TestCodeLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="testCodeId")
	private TestCode testCodeId;
	private String testCode;
	private String disPalyTestCode;
	private int orderNo;
	@ManyToOne
	@JoinColumn(name="testCodeUintsId")
	private TestCodeUnits testCodeUints;	
	private boolean activeStatus = true;
	private String type;
	private boolean requiredUnits = true;
	
	public TestCode getTestCodeId() {
		return testCodeId;
	}
	public void setTestCodeId(TestCode testCodeId) {
		this.testCodeId = testCodeId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTestCode() {
		return testCode;
	}
	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}
	public String getDisPalyTestCode() {
		return disPalyTestCode;
	}
	public void setDisPalyTestCode(String disPalyTestCode) {
		this.disPalyTestCode = disPalyTestCode;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public TestCodeUnits getTestCodeUints() {
		return testCodeUints;
	}
	public void setTestCodeUints(TestCodeUnits testCodeUints) {
		this.testCodeUints = testCodeUints;
	}
	public boolean isActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isRequiredUnits() {
		return requiredUnits;
	}
	public void setRequiredUnits(boolean requiredUnits) {
		this.requiredUnits = requiredUnits;
	}
	
	
}
