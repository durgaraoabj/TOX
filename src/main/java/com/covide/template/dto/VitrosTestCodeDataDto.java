package com.covide.template.dto;

import java.io.Serializable;
import java.util.List;

import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.VitrosData;

public class VitrosTestCodeDataDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8197013655444740777L;
	private StudyTestCodes testCode;
	private int orderNo;
	private List<VitrosData> vitroData;
	public StudyTestCodes getTestCode() {
		return testCode;
	}
	public void setTestCode(StudyTestCodes testCode) {
		this.testCode = testCode;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public List<VitrosData> getVitroData() {
		return vitroData;
	}
	public void setVitroData(List<VitrosData> vitroData) {
		this.vitroData = vitroData;
	}
	
	
}
