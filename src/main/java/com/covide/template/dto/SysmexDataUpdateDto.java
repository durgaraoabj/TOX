package com.covide.template.dto;

import java.io.Serializable;
import java.util.List;

import com.springmvc.model.SysmexTestCodeData;
import com.springmvc.model.VitrosData;

public class SysmexDataUpdateDto implements Serializable{
	private String studyNumber;
	private String testName;
	private List<SysmexTestCodeDataDto> resultDto;
	private List<SysmexTestCodeData> result;
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
	public List<SysmexTestCodeData> getResult() {
		return result;
	}
	public void setResult(List<SysmexTestCodeData> result) {
		this.result = result;
	}
	public List<SysmexTestCodeDataDto> getResultDto() {
		return resultDto;
	}
	public void setResultDto(List<SysmexTestCodeDataDto> resultDto) {
		this.resultDto = resultDto;
	}
	
	
}
