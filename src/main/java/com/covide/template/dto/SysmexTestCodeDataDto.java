package com.covide.template.dto;

import java.io.Serializable;

import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SysmexTestCodeData;

public class SysmexTestCodeDataDto implements Serializable{
	private StudyTestCodes testCode;

	private String resultToDisplay ="";
	private boolean mutipleReuslt;

	private SysmexTestCodeData sysmexTestCodeData;
	private Integer orderNo;
	private String displayTestCode = "";
	private String testType;//actual or persent
	private boolean usedPersent;
	
	private boolean finalValue = true;
	private Long id;
	private String value;
	private String displayRunTime = "";
	
	
	public boolean isFinalValue() {
		return finalValue;
	}
	public void setFinalValue(boolean finalValue) {
		this.finalValue = finalValue;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDisplayRunTime() {
		return displayRunTime;
	}
	public void setDisplayRunTime(String displayRunTime) {
		this.displayRunTime = displayRunTime;
	}
	public SysmexTestCodeData getSysmexTestCodeData() {
		return sysmexTestCodeData;
	}
	public void setSysmexTestCodeData(SysmexTestCodeData sysmexTestCodeData) {
		this.sysmexTestCodeData = sysmexTestCodeData;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public boolean isUsedPersent() {
		return usedPersent;
	}
	public void setUsedPersent(boolean usedPersent) {
		this.usedPersent = usedPersent;
	}
	public String getDisplayTestCode() {
		return displayTestCode;
	}
	public void setDisplayTestCode(String displayTestCode) {
		this.displayTestCode = displayTestCode;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public StudyTestCodes getTestCode() {
		return testCode;
	}
	public void setTestCode(StudyTestCodes testCode) {
		this.testCode = testCode;
	}
	public String getResultToDisplay() {
		return resultToDisplay;
	}
	public void setResultToDisplay(String resultToDisplay) {
		this.resultToDisplay = resultToDisplay;
	}
	public boolean isMutipleReuslt() {
		return mutipleReuslt;
	}
	public void setMutipleReuslt(boolean mutipleReuslt) {
		this.mutipleReuslt = mutipleReuslt;
	}
	public SysmexTestCodeDataDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SysmexTestCodeDataDto(StudyTestCodes testCode, String resultToDisplay, boolean mutipleReuslt) {
		super();
		this.testCode = testCode;
		this.resultToDisplay = resultToDisplay;
		this.mutipleReuslt = mutipleReuslt;
	}
	
	
	
}
