package com.covide.template.dto;

import java.io.Serializable;
import java.util.List;

import com.springmvc.model.VitrosData;

public class VistrosDataUpdateDto implements Serializable{

	private String studyNumber;
	private String testName;
	private List<VitrosData> result;
	public String getStudyNumber() {
		return studyNumber;
	}
	public void setStudyNumber(String studyNumber) {
		this.studyNumber = studyNumber;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public List<VitrosData> getResult() {
		return result;
	}
	public void setResult(List<VitrosData> result) {
		this.result = result;
	}
	
	
}
