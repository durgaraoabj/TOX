package com.springmvc.util;

import java.util.Map;
import java.util.Set;

import com.springmvc.model.StudyMaster;

public class MotorActivityDataOfIndividualAnimal {
	private String type = "";
	private StudyMaster study;
	private int groupNumber;
	private String gender;
	private Set<Integer> animalNumbers;
	private Map<Integer, MotorActivityDataOfIndividualAnimalData> data;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public int getGroupNumber() {
		return groupNumber;
	}
	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Set<Integer> getAnimalNumbers() {
		return animalNumbers;
	}
	public void setAnimalNumbers(Set<Integer> animalNumbers) {
		this.animalNumbers = animalNumbers;
	}
	public Map<Integer, MotorActivityDataOfIndividualAnimalData> getData() {
		return data;
	}
	public void setData(Map<Integer, MotorActivityDataOfIndividualAnimalData> data) {
		this.data = data;
	}
	
}
