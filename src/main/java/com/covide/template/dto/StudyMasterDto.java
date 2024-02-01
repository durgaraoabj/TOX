package com.covide.template.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SubGroupAnimalsInfo;

public class StudyMasterDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5191026915263014152L;
	private Long studId;
	private String studyNo;
	private List<SubGroupAnimalsInfo> groups;
	private List<StudyAnimal> animals;
	private Map<String, List<StudyTestCodes>> insturmentViewTestCodes = new LinkedHashMap<>();
	public Long getStudId() {
		return studId;
	}
	public void setStudId(Long studId) {
		this.studId = studId;
	}
	public String getStudyNo() {
		return studyNo;
	}
	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
	}
	public List<SubGroupAnimalsInfo> getGroups() {
		return groups;
	}
	public void setGroups(List<SubGroupAnimalsInfo> groups) {
		this.groups = groups;
	}
	public Map<String, List<StudyTestCodes>> getInsturmentViewTestCodes() {
		return insturmentViewTestCodes;
	}
	public void setInsturmentViewTestCodes(Map<String, List<StudyTestCodes>> insturmentViewTestCodes) {
		this.insturmentViewTestCodes = insturmentViewTestCodes;
	}
	public List<StudyAnimal> getAnimals() {
		return animals;
	}
	public void setAnimals(List<StudyAnimal> animals) {
		this.animals = animals;
	}
	
	
}
