package com.springmvc.util;

import java.util.ArrayList;
import java.util.List;

import com.springmvc.model.StudyMaster;

public class DetailedClinicalObservations {
	private String type = "";
	private StudyMaster study;
	List<DetailedClinicalObservationsAnimlas> eachAnimal = new ArrayList<>();
	
	OphthalmologicalExamination eds;
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
	public List<DetailedClinicalObservationsAnimlas> getEachAnimal() {
		return eachAnimal;
	}
	public void setEachAnimal(List<DetailedClinicalObservationsAnimlas> eachAnimal) {
		this.eachAnimal = eachAnimal;
	}
	
}
