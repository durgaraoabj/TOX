package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="study_accession_animals")
public class StudyAccessionAnimals extends CommonMaster implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3921063512442174744L;
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyAccessionAnimals_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="prefix")
	private String prefix;
	@Column(name="gender")
	private String gender;
	@Column(name="animals_from")
	private String animalsFrom;
	@Column(name="animals_to")
	private String animalsTo;
	@Column(name="no_of_animals")
	private String noOfAnimals;
	@Column(name="total_animals")
	private int totalAnimals;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster study;

	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAnimalsFrom() {
		return animalsFrom;
	}
	public void setAnimalsFrom(String animalsFrom) {
		this.animalsFrom = animalsFrom;
	}
	public String getAnimalsTo() {
		return animalsTo;
	}
	public void setAnimalsTo(String animalsTo) {
		this.animalsTo = animalsTo;
	}
	public String getNoOfAnimals() {
		return noOfAnimals;
	}
	public void setNoOfAnimals(String noOfAnimals) {
		this.noOfAnimals = noOfAnimals;
	}
	public int getTotalAnimals() {
		return totalAnimals;
	}
	public void setTotalAnimals(int totalAnimals) {
		this.totalAnimals = totalAnimals;
	}

	
	
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}

}
