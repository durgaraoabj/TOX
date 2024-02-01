package com.covide.dto;

import java.io.Serializable;
import java.util.List;

public class StudyAccessionCrfSectionElementDataUpdateDto implements Serializable{
	private Long eleId; //		eleId = StudyAccessionCrfSectionElementData.id
	private String newValue;
	private List<String> newValues;
	private String eleType;
	private String comment;
	public Long getEleId() {
		return eleId;
	}
	public void setEleId(Long eleId) {
		this.eleId = eleId;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public List<String> getNewValues() {
		return newValues;
	}
	public void setNewValues(List<String> newValues) {
		this.newValues = newValues;
	}
	public String getEleType() {
		return eleType;
	}
	public void setEleType(String eleType) {
		this.eleType = eleType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
