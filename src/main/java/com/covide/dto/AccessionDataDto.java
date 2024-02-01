package com.covide.dto;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class AccessionDataDto implements Serializable {
	
	private String animalStr;
	private String animalId;
	private int genderSize;
	private Date studyDate;
	private boolean dateDeviation;
	private Date actualDate;
	private Date weekStDate;
	private Date weekEndDate;
	public String getAnimalStr() {
		return animalStr;
	}
	public void setAnimalStr(String animalStr) {
		this.animalStr = animalStr;
	}
	
	public String getAnimalId() {
		return animalId;
	}
	public void setAnimalId(String animalId) {
		this.animalId = animalId;
	}
	public int getGenderSize() {
		return genderSize;
	}
	public void setGenderSize(int genderSize) {
		this.genderSize = genderSize;
	}
	public Date getStudyDate() {
		return studyDate;
	}
	public void setStudyDate(Date studyDate) {
		this.studyDate = studyDate;
	}
	public boolean isDateDeviation() {
		return dateDeviation;
	}
	public void setDateDeviation(boolean dateDeviation) {
		this.dateDeviation = dateDeviation;
	}
	public Date getActualDate() {
		return actualDate;
	}
	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}
	public Date getWeekStDate() {
		return weekStDate;
	}
	public void setWeekStDate(Date weekStDate) {
		this.weekStDate = weekStDate;
	}
	public Date getWeekEndDate() {
		return weekEndDate;
	}
	public void setWeekEndDate(Date weekEndDate) {
		this.weekEndDate = weekEndDate;
	}
	
	
	
	
	

}
