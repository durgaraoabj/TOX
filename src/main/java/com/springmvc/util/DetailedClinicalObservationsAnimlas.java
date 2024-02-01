package com.springmvc.util;

import com.springmvc.model.StudyAnimal;

public class DetailedClinicalObservationsAnimlas implements Comparable<DetailedClinicalObservationsAnimlas> {
	private String type = "";
	private int sno;
	private StudyAnimal animal;
	private String value ="";
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int compareTo(DetailedClinicalObservationsAnimlas o) {
		// TODO Auto-generated method stub
		if(type.equals("Treatment"))
			return this.getAnimal().getAnimalId() - o.getAnimal().getAnimalId();
		else
			return this.getAnimal().getSequnceNo() - o.getAnimal().getSequnceNo();
	}
	
}
