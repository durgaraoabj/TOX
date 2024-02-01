package com.springmvc.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
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
@Table(name="RandamizationDto")
public class RandamizationDto extends CommonMaster{

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="RandamizationDto_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="studyId")
	private StudyMaster study;
	private boolean activeStatus = true;
	@ManyToOne
	@JoinColumn(name="approvalStatusId")
	private StatusMaster approvalStatus;
	@ManyToOne
	@JoinColumn(name="reviwedBy")
	private LoginUsers reviwedBy;
	private Date reviwedOn;
	private String reviewComment;
	private String gender ="";
	@Transient
	private List<StudyAnimal> animals;
	@Transient
	private List<RandamizationAssigndDto> randamizationAssigndDto;
	private Double mean;
	private Double maxMean;
	private Double minMean;
	private Double maxWeight;
	private Double minWeight;
	@Column(name="maxAnimalNo")
	private int maxAnimalNo;
	@Transient
	private List<RandamizationGroupDto> randamizationGroupDto;
	
	
	public int getMaxAnimalNo() {
		return maxAnimalNo;
	}
	public void setMaxAnimalNo(int maxAnimalNo) {
		this.maxAnimalNo = maxAnimalNo;
	}
	public LoginUsers getReviwedBy() {
		return reviwedBy;
	}
	public void setReviwedBy(LoginUsers reviwedBy) {
		this.reviwedBy = reviwedBy;
	}
	public Date getReviwedOn() {
		return reviwedOn;
	}
	public void setReviwedOn(Date reviwedOn) {
		this.reviwedOn = reviwedOn;
	}
	public Double getMean() {
		return mean;
	}
	public void setMean(Double mean) {
		this.mean = mean;
	}
	public Double getMaxMean() {
		return maxMean;
	}
	public void setMaxMean(Double maxMean) {
		this.maxMean = maxMean;
	}
	public Double getMinMean() {
		return minMean;
	}
	public void setMinMean(Double minMean) {
		this.minMean = minMean;
	}
	public Double getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}
	public Double getMinWeight() {
		return minWeight;
	}
	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public boolean isActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	public StatusMaster getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(StatusMaster approvalStatus) {
		this.approvalStatus = approvalStatus;
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
	public List<RandamizationAssigndDto> getRandamizationAssigndDto() {
		return randamizationAssigndDto;
	}
	public void setRandamizationAssigndDto(List<RandamizationAssigndDto> randamizationAssigndDto) {
		this.randamizationAssigndDto = randamizationAssigndDto;
	}
	public List<RandamizationGroupDto> getRandamizationGroupDto() {
		return randamizationGroupDto;
	}
	public void setRandamizationGroupDto(List<RandamizationGroupDto> randamizationGroupDto) {
		this.randamizationGroupDto = randamizationGroupDto;
	}
	public String getReviewComment() {
		return reviewComment;
	}
	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}
	
}
