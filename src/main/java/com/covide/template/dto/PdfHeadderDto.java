package com.covide.template.dto;

import java.io.Serializable;

import com.springmvc.model.StudyMaster;

public class PdfHeadderDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6210252147811584412L;
	private String studyNumber = "";
	private String testRunTime = "";
	private String gorupNam = "";
	private String animalNo = "";
	private String instrument = "";
	private StudyMaster study;
	
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public String getStudyNumber() {
		return studyNumber;
	}
	public void setStudyNumber(String studyNumber) {
		this.studyNumber = studyNumber;
	}
	public String getTestRunTime() {
		return testRunTime;
	}
	public void setTestRunTime(String testRunTime) {
		this.testRunTime = testRunTime;
	}
	public String getGorupNam() {
		return gorupNam;
	}
	public void setGorupNam(String gorupNam) {
		this.gorupNam = gorupNam;
	}
	public String getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	
}
