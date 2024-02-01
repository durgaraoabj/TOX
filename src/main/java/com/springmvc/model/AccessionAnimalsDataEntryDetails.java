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

import com.covide.crf.dto.Crf;




@Entity
@Table(name="accession_animals_data_entry_details")
public class AccessionAnimalsDataEntryDetails extends CommonMaster implements Comparable<AccessionAnimalsDataEntryDetails>, Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="AccessionAnimalsDataEntryDetails_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	
	
	@ManyToOne
	@JoinColumn(name="crf_id")
	private Crf crf;	
	@ManyToOne
	@JoinColumn(name="animal_id")
	private StudyAnimal animal;
	private String gender = "";
	@Column(name="animal_temp_id")
	private String animalTempId;
	@Column(name="entry_type")
	private String entryType = "scheduled";
	@ManyToOne
	@JoinColumn(name="studyAcclamatizationDataId")
	private StudyAcclamatizationData studyAcclamatizationData;
	@ManyToOne
	@JoinColumn(name="studyAcclamatizationDatesId")
	private StudyAcclamatizationDates studyAcclamatizationDates;
	private int noOfEntry;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster study;
	@Column(name="deviation")
	private String deviation;
	@ManyToOne
	@JoinColumn(name="study_acc_animal")
	private StudyAccessionAnimals studyAccAnimal;
	@ManyToOne
	@JoinColumn(name="status")
	private StatusMaster status; //approved, in-review, 
	
	@Column(name="approved_by")
	private String approvedBy;
	@Column(name="approved_on")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date approvedOn;
	@Column(name="rejected_by")
	private String rejectedBy;
	@Column(name="rejected_on")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date rejectedOn;
	private String reviewComment;
	@ManyToOne
	@JoinColumn(name="currentReviewRole")
	private RoleMaster currentReviewRole;
	
	private String sendBy;
	private Date sentOn;
	
	
	public StudyAcclamatizationData getStudyAcclamatizationData() {
		return studyAcclamatizationData;
	}
	public void setStudyAcclamatizationData(StudyAcclamatizationData studyAcclamatizationData) {
		this.studyAcclamatizationData = studyAcclamatizationData;
	}
	public StudyAcclamatizationDates getStudyAcclamatizationDates() {
		return studyAcclamatizationDates;
	}
	public void setStudyAcclamatizationDates(StudyAcclamatizationDates studyAcclamatizationDates) {
		this.studyAcclamatizationDates = studyAcclamatizationDates;
	}
	public int getNoOfEntry() {
		return noOfEntry;
	}
	public void setNoOfEntry(int noOfEntry) {
		this.noOfEntry = noOfEntry;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSendBy() {
		return sendBy;
	}
	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}
	public Date getSentOn() {
		return sentOn;
	}
	public void setSentOn(Date sentOn) {
		this.sentOn = sentOn;
	}
	public String getReviewComment() {
		return reviewComment;
	}
	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}
	public String getRejectedBy() {
		return rejectedBy;
	}
	public void setRejectedBy(String rejectedBy) {
		this.rejectedBy = rejectedBy;
	}
	public Date getRejectedOn() {
		return rejectedOn;
	}
	public void setRejectedOn(Date rejectedOn) {
		this.rejectedOn = rejectedOn;
	}
	public RoleMaster getCurrentReviewRole() {
		return currentReviewRole;
	}
	public void setCurrentReviewRole(RoleMaster currentReviewRole) {
		this.currentReviewRole = currentReviewRole;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public String getEntryType() {
		return entryType;
	}
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	
	public String getAnimalTempId() {
		return animalTempId;
	}
	public void setAnimalTempId(String animalTempId) {
		this.animalTempId = animalTempId;
	}
	public String getDeviation() {
		return deviation;
	}
	public void setDeviation(String deviation) {
		this.deviation = deviation;
	}
	public StudyAccessionAnimals getStudyAccAnimal() {
		return studyAccAnimal;
	}
	public void setStudyAccAnimal(StudyAccessionAnimals studyAccAnimal) {
		this.studyAccAnimal = studyAccAnimal;
	}
	
	public StatusMaster getStatus() {
		return status;
	}
	public void setStatus(StatusMaster status) {
		this.status = status;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public Date getApprovedOn() {
		return approvedOn;
	}
	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}
	@Override
	public int compareTo(AccessionAnimalsDataEntryDetails arg0) {
		// TODO Auto-generated method stub
//		return getApprovedOn().compareTo(arg0.getApprovedOn());
		return arg0.getApprovedOn().compareTo(getApprovedOn());
	}
	
	

}
