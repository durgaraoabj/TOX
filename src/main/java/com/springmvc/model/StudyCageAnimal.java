package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Study_Cage_Animal")
public class StudyCageAnimal extends CommonMaster implements Serializable{
	/**
	 * 
	 */
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyCageAnimal_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "studyId")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name = "cageId")
	private AnimalCage cage;
	@ManyToOne
	@JoinColumn(name = "animalId")
	private StudyAnimal animal;
	@Transient
	private boolean allowAnimalToDataEntry = true;
	@Transient
	private String userMessage;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isAllowAnimalToDataEntry() {
		return allowAnimalToDataEntry;
	}
	public void setAllowAnimalToDataEntry(boolean allowAnimalToDataEntry) {
		this.allowAnimalToDataEntry = allowAnimalToDataEntry;
	}
	public String getUserMessage() {
		return userMessage;
	}
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public AnimalCage getCage() {
		return cage;
	}
	public void setCage(AnimalCage cage) {
		this.cage = cage;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	
}
