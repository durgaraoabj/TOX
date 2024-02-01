package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "radomization_review_dto")
public class RadomizationReviewDto extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="RadomizationReviewDto_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private int errorCode = 0;
	private String errorMessage = "";
	@ManyToOne
	@JoinColumn(name="studyId")
	private StudyMaster study;
	private String gender = "";
	private boolean viewData = true;
	@ManyToOne
	@JoinColumn(name = "reviewStatus")
	private StatusMaster reviewStatus;
	@ManyToOne
	@JoinColumn(name = "randamizaitonSheetMale")
	private RadamizationAllDataReview randamizaitonSheetMale;
	@ManyToOne
	@JoinColumn(name = "randamizaitonSheetFemale")
	private RadamizationAllDataReview randamizaitonSheetFemale;
	@ManyToOne
	@JoinColumn(name = "randamizaitonSheetMaleAscedning")
	private RadamizationAllDataReview randamizaitonSheetMaleAscedning;
	@ManyToOne
	@JoinColumn(name = "randamizaitonSheetFemaleAscedning")
	private RadamizationAllDataReview randamizaitonSheetFemaleAscedning;
	@ManyToMany(targetEntity = RadamizationAllDataReview.class, cascade = { CascadeType.ALL })  
	@JoinTable(name = "tox_randamizaitonSheet_maleGruoup",   
	            joinColumns = { @JoinColumn(name = "radomization_review_dtoId") },   
	            inverseJoinColumns = { @JoinColumn(name = "radamizationAllDataReviewId") })  
	private List<RadamizationAllDataReview> randamizaitonSheetMaleGruoup;
	@ManyToMany(targetEntity = RadamizationAllDataReview.class, cascade = { CascadeType.ALL })  
	@JoinTable(name = "tox_randamizaitonSheet_femaleGruoup",   
	            joinColumns = { @JoinColumn(name = "radomization_review_dtoId") },   
	            inverseJoinColumns = { @JoinColumn(name = "radamizationAllDataReviewId") })  
	private List<RadamizationAllDataReview> randamizaitonSheetFemaleGruoup;
	
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
	@Transient
	private boolean reviewed;
	@Transient
	private boolean allowToReview ;
	@Transient
	private String displayMessage;
	
	
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
	public String getDisplayMessage() {
		return displayMessage;
	}
	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}
	public boolean isAllowToReview() {
		return allowToReview;
	}
	public void setAllowToReview(boolean allowToReview) {
		this.allowToReview = allowToReview;
	}
	public boolean isReviewed() {
		return reviewed;
	}
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public StatusMaster getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(StatusMaster reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public RadamizationAllDataReview getRandamizaitonSheetMale() {
		return randamizaitonSheetMale;
	}
	public void setRandamizaitonSheetMale(RadamizationAllDataReview randamizaitonSheetMale) {
		this.randamizaitonSheetMale = randamizaitonSheetMale;
	}
	public RadamizationAllDataReview getRandamizaitonSheetFemale() {
		return randamizaitonSheetFemale;
	}
	public void setRandamizaitonSheetFemale(RadamizationAllDataReview randamizaitonSheetFemale) {
		this.randamizaitonSheetFemale = randamizaitonSheetFemale;
	}
	public RadamizationAllDataReview getRandamizaitonSheetMaleAscedning() {
		return randamizaitonSheetMaleAscedning;
	}
	public void setRandamizaitonSheetMaleAscedning(RadamizationAllDataReview randamizaitonSheetMaleAscedning) {
		this.randamizaitonSheetMaleAscedning = randamizaitonSheetMaleAscedning;
	}
	public RadamizationAllDataReview getRandamizaitonSheetFemaleAscedning() {
		return randamizaitonSheetFemaleAscedning;
	}
	public void setRandamizaitonSheetFemaleAscedning(RadamizationAllDataReview randamizaitonSheetFemaleAscedning) {
		this.randamizaitonSheetFemaleAscedning = randamizaitonSheetFemaleAscedning;
	}
	public List<RadamizationAllDataReview> getRandamizaitonSheetMaleGruoup() {
		return randamizaitonSheetMaleGruoup;
	}
	public void setRandamizaitonSheetMaleGruoup(List<RadamizationAllDataReview> randamizaitonSheetMaleGruoup) {
		this.randamizaitonSheetMaleGruoup = randamizaitonSheetMaleGruoup;
	}
	public List<RadamizationAllDataReview> getRandamizaitonSheetFemaleGruoup() {
		return randamizaitonSheetFemaleGruoup;
	}
	public void setRandamizaitonSheetFemaleGruoup(List<RadamizationAllDataReview> randamizaitonSheetFemaleGruoup) {
		this.randamizaitonSheetFemaleGruoup = randamizaitonSheetFemaleGruoup;
	}
	public RoleMaster getCurrentReviewRole() {
		return currentReviewRole;
	}
	public void setCurrentReviewRole(RoleMaster currentReviewRole) {
		this.currentReviewRole = currentReviewRole;
	}
	public boolean isViewData() {
		return viewData;
	}
	public void setViewData(boolean viewData) {
		this.viewData = viewData;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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
	
	
	
}
