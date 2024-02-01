package com.springmvc.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table
@Entity(name = "radamization_all_data_review")
public class RadamizationAllDataReview extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="RadamizationAllDataReview_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster study;
	private String gender;
	@ManyToMany(targetEntity = StudyAnimal.class, cascade = { CascadeType.MERGE }, fetch=FetchType.EAGER)  
	@JoinTable(name = "tox_radamization_all_data_review_animals",   
	            joinColumns = { @JoinColumn(name = "RadamizationAllDataReviewId") },   
	            inverseJoinColumns = { @JoinColumn(name = "animalsId") })  
	private List<StudyAnimal> animals;
	@Column(name="data_mean")
	private Double mean;
	@Column(name="data_start")
	private Double start;
	@Column(name="data_end")
	private Double end;
	@Column(name="data_minimum")
	private Double minimum;
	@Column(name="data_maximum")
	private Double maximum;
	@ManyToOne
	@JoinColumn(name="group_id")
	private GroupInfo group;
	private int gorupNo;
	private Double groupMean;
	private Double grouoSD;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	public int getGorupNo() {
		return gorupNo;
	}
	public void setGorupNo(int gorupNo) {
		this.gorupNo = gorupNo;
	}
	public Double getGroupMean() {
		return groupMean;
	}
	public void setGroupMean(Double groupMean) {
		this.groupMean = groupMean;
	}
	public Double getGrouoSD() {
		return grouoSD;
	}
	public void setGrouoSD(Double grouoSD) {
		this.grouoSD = grouoSD;
	}
	public Double getMean() {
		return mean;
	}
	public void setMean(Double mean) {
		this.mean = mean;
	}
	public Double getStart() {
		return start;
	}
	public void setStart(Double start) {
		this.start = start;
	}
	public Double getEnd() {
		return end;
	}
	public void setEnd(Double end) {
		this.end = end;
	}
	public Double getMinimum() {
		return minimum;
	}
	public void setMinimum(Double minimum) {
		this.minimum = minimum;
	}
	public Double getMaximum() {
		return maximum;
	}
	public void setMaximum(Double maximum) {
		this.maximum = maximum;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	public List<StudyAnimal> getAnimals() {
		return animals;
	}
	public void setAnimals(List<StudyAnimal> animals) {
		this.animals = animals;
	}
	public RadamizationAllDataReview(StudyMaster study, String gender, List<StudyAnimal> animals) {
		super();
		this.study = study;
		this.gender = gender;
		this.animals = animals;
	}
	public RadamizationAllDataReview() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
