package com.covide.crf.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObservationClinPathTestCodesDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8568599511777518859L;
	public String opration = "";
	public String insturment ="";
	public List<StudyTestCodesDto> testCodes = new ArrayList<>();
	public String getInsturment() {
		return insturment;
	}
	public void setInsturment(String insturment) {
		this.insturment = insturment;
	}
	public List<StudyTestCodesDto> getTestCodes() {
		return testCodes;
	}
	public void setTestCodes(List<StudyTestCodesDto> testCodes) {
		this.testCodes = testCodes;
	}
	public String getOpration() {
		return opration;
	}
	public void setOpration(String opration) {
		this.opration = opration;
	}

	
	
}
