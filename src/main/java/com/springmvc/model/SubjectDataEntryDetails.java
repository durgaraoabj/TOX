package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="subject_data_entry_details")
public class SubjectDataEntryDetails extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SubjectDataEntryDetails_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="studyId")
	private StudyMaster study;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="groupId")
	private GroupInfo group;
	@ManyToOne
	@JoinColumn(name="subGroupId")
	private SubGroupInfo subGroup;
	@Column(name="entry_type")
	private String entryType = "scheduled";
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="subGroupAnaimalId")
	private SubGroupAnimalsInfo subGroupAnaimal;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cageId")
	private AnimalCage cage;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="studyAnimalCageId")
	private StudyCageAnimal studyAnimalCage;
	@ManyToOne
	@JoinColumn(name="animalId")
	private StudyAnimal animal;
	private String gender = "";
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="observationCrfId")
	private StdSubGroupObservationCrfs observationCrf;
	@ManyToOne
	@JoinColumn(name="studyTreatmentDataDatesId")
	private StudyTreatmentDataDates studyTreatmentDataDates;
	private int noOfEntry;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="deprtmentId")
	private DepartmentMaster deprtment;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="entredby")
	private LoginUsers entredby;
	private Date entredOn = new Date();
	private String entredAs;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="approvalStatus")
	private StatusMaster status;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="approvedBy")
	private LoginUsers approvedBy;
	private Date approvedOn;
	private int animalno;
	@Column(name="rejected_by")
	private String rejectedBy;
	@Column(name="rejected_on")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date rejectedOn;
	private String reviewComment;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="currentReviewRole")
	private RoleMaster currentReviewRole;
	
	private String days;
	private String deviationSign;
	private String deviation;
	private String dataEntry = "";
	private String type = "";
	
	private String sendBy;
	private Date sentOn;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getReviewComment() {
		return reviewComment;
	}
	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}
	public RoleMaster getCurrentReviewRole() {
		return currentReviewRole;
	}
	public void setCurrentReviewRole(RoleMaster currentReviewRole) {
		this.currentReviewRole = currentReviewRole;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDataEntry() {
		return dataEntry;
	}
	public void setDataEntry(String dataEntry) {
		this.dataEntry = dataEntry;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getDeviationSign() {
		return deviationSign;
	}
	public void setDeviationSign(String deviationSign) {
		this.deviationSign = deviationSign;
	}
	
	public String getDeviation() {
		return deviation;
	}
	public void setDeviation(String deviation) {
		this.deviation = deviation;
	}
	public int getAnimalno() {
		return animalno;
	}
	public void setAnimalno(int animalno) {
		this.animalno = animalno;
	}
	public LoginUsers getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(LoginUsers approvedBy) {
		this.approvedBy = approvedBy;
	}
	public Date getApprovedOn() {
		return approvedOn;
	}
	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	public SubGroupInfo getSubGroup() {
		return subGroup;
	}
	public void setSubGroup(SubGroupInfo subGroup) {
		this.subGroup = subGroup;
	}
	public SubGroupAnimalsInfo getSubGroupAnaimal() {
		return subGroupAnaimal;
	}
	public void setSubGroupAnaimal(SubGroupAnimalsInfo subGroupAnaimal) {
		this.subGroupAnaimal = subGroupAnaimal;
	}
	public AnimalCage getCage() {
		return cage;
	}
	public void setCage(AnimalCage cage) {
		this.cage = cage;
	}
	public StudyCageAnimal getStudyAnimalCage() {
		return studyAnimalCage;
	}
	public void setStudyAnimalCage(StudyCageAnimal studyAnimalCage) {
		this.studyAnimalCage = studyAnimalCage;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public StdSubGroupObservationCrfs getObservationCrf() {
		return observationCrf;
	}
	public void setObservationCrf(StdSubGroupObservationCrfs observationCrf) {
		this.observationCrf = observationCrf;
	}
	public DepartmentMaster getDeprtment() {
		return deprtment;
	}
	public void setDeprtment(DepartmentMaster deprtment) {
		this.deprtment = deprtment;
	}
	public LoginUsers getEntredby() {
		return entredby;
	}
	public void setEntredby(LoginUsers entredby) {
		this.entredby = entredby;
	}
	public Date getEntredOn() {
		return entredOn;
	}
	public void setEntredOn(Date entredOn) {
		this.entredOn = entredOn;
	}
	public String getEntredAs() {
		return entredAs;
	}
	public void setEntredAs(String entredAs) {
		this.entredAs = entredAs;
	}
	
	public StatusMaster getStatus() {
		return status;
	}
	public void setStatus(StatusMaster status) {
		this.status = status;
	}
	public String getEntryType() {
		return entryType;
	}
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	public int getNoOfEntry() {
		return noOfEntry;
	}
	public void setNoOfEntry(int noOfEntry) {
		this.noOfEntry = noOfEntry;
	}
	public StudyTreatmentDataDates getStudyTreatmentDataDates() {
		return studyTreatmentDataDates;
	}
	public void setStudyTreatmentDataDates(StudyTreatmentDataDates studyTreatmentDataDates) {
		this.studyTreatmentDataDates = studyTreatmentDataDates;
	}
	
}
