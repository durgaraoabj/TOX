package com.springmvc.util;

import java.util.ArrayList;
import java.util.List;

import com.springmvc.model.StudyMaster;

public class OphthalmologicalExamination {
	private String type = "";
	private StudyMaster study;
	List<OphthalmologicalExaminationAnimlas> eachAnimal = new ArrayList<>();
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
	public List<OphthalmologicalExaminationAnimlas> getEachAnimal() {
		return eachAnimal;
	}
	public void setEachAnimal(List<OphthalmologicalExaminationAnimlas> eachAnimal) {
		this.eachAnimal = eachAnimal;
	}
	
}
