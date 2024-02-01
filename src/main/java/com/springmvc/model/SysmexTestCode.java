package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author swami.tammireddi
 *
 */
@Entity
@Table(name = "SYSMEX_TEST_CODE")
public class SysmexTestCode extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SysmexTestCode_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String testCode;
	private String disPalyTestCode;
	private int orderNo;
	private String testUints;	
	private boolean activeStatus = true;
	private boolean requiredUnits = true;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isRequiredUnits() {
		return requiredUnits;
	}

	public void setRequiredUnits(boolean requiredUnits) {
		this.requiredUnits = requiredUnits;
	}

	

	public String getDisPalyTestCode() {
		return disPalyTestCode;
	}

	public void setDisPalyTestCode(String disPalyTestCode) {
		this.disPalyTestCode = disPalyTestCode;
	}

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getTestUints() {
		return testUints;
	}

	public void setTestUints(String testUints) {
		this.testUints = testUints;
	}
	
}
