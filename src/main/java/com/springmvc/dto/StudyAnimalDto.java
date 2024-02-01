package com.springmvc.dto;

import java.io.Serializable;

public class StudyAnimalDto implements Serializable{
	private Long id;
	private String accessionNo;
	private String animalNo;
	private int animalId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccessionNo() {
		return accessionNo;
	}
	public void setAccessionNo(String accessionNo) {
		this.accessionNo = accessionNo;
	}
	public String getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}
	public int getAnimalId() {
		return animalId;
	}
	public void setAnimalId(int animalId) {
		this.animalId = animalId;
	}
	
	
}
