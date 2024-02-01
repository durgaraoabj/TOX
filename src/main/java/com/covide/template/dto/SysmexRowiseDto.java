package com.covide.template.dto;

import java.io.Serializable;
import java.util.SortedMap;

import com.springmvc.model.GroupInfo;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;

public class SysmexRowiseDto implements Serializable{
	private StudyMaster study;
	private GroupInfo group;
	private StudyAnimal animal;
	private String runTime = "";
	
	private SortedMap<Integer, SysmexTestCodeDataDto> elementValues;

	public StudyMaster getStudy() {
		return study;
	}

	public void setStudy(StudyMaster study) {
		this.study = study;
	}

	public GroupInfo getGroup() {
		return group;
	}

	public void setGroup(GroupInfo group) {
		this.group = group;
	}

	public StudyAnimal getAnimal() {
		return animal;
	}

	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public SortedMap<Integer, SysmexTestCodeDataDto> getElementValues() {
		return elementValues;
	}

	public void setElementValues(SortedMap<Integer, SysmexTestCodeDataDto> elementValues) {
		this.elementValues = elementValues;
	}

	public SysmexRowiseDto(StudyMaster study, GroupInfo group, StudyAnimal animal, String runTime,
			SortedMap<Integer, SysmexTestCodeDataDto> elementValues) {
		super();
		this.study = study;
		this.group = group;
		this.animal = animal;
		this.runTime = runTime;
		this.elementValues = elementValues;
	}

	public SysmexRowiseDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
