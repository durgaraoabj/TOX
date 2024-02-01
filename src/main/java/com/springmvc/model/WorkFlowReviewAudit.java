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

import com.covide.dto.RadomizationDto;

@Entity
@Table(name = "Work_Flow_Review_Audit")
public class WorkFlowReviewAudit extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="WorkFlowReviewAudit_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "study_id")
	private StudyMaster study;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "accessionAnimalsDataEntryDetailsId")
	private AccessionAnimalsDataEntryDetails accessionAnimalsDataEntryDetailsId;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "subjectDataEntryDetailsId")
	private SubjectDataEntryDetails subjectDataEntryDetails;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "reamdamizationReivew_id")
	private RadomizationReviewDto reamdamizationReivew;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "randamizationDtoId")
	private RandamizationDto randamizationDto;
//	@ManyToOne
//	@JoinColumn(name = "eform_id")
//	private EForm eform;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "reviewState_id")
	private WorkFlowReviewStages reviewState;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private RoleMaster role;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private LoginUsers user;
	@Column(name="date_of_activity")
	private Date dateOfActivity;
	private String comment;
	private String status = "";  
	@Column(name="in_the_flow")
	private boolean inTheFlow = true;
	
	
	public RandamizationDto getRandamizationDto() {
		return randamizationDto;
	}
	public void setRandamizationDto(RandamizationDto randamizationDto) {
		this.randamizationDto = randamizationDto;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SubjectDataEntryDetails getSubjectDataEntryDetails() {
		return subjectDataEntryDetails;
	}
	public void setSubjectDataEntryDetails(SubjectDataEntryDetails subjectDataEntryDetails) {
		this.subjectDataEntryDetails = subjectDataEntryDetails;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public RadomizationReviewDto getReamdamizationReivew() {
		return reamdamizationReivew;
	}
	public void setReamdamizationReivew(RadomizationReviewDto reamdamizationReivew) {
		this.reamdamizationReivew = reamdamizationReivew;
	}
	public AccessionAnimalsDataEntryDetails getAccessionAnimalsDataEntryDetailsId() {
		return accessionAnimalsDataEntryDetailsId;
	}
	public void setAccessionAnimalsDataEntryDetailsId(AccessionAnimalsDataEntryDetails accessionAnimalsDataEntryDetailsId) {
		this.accessionAnimalsDataEntryDetailsId = accessionAnimalsDataEntryDetailsId;
	}
	public WorkFlowReviewStages getReviewState() {
		return reviewState;
	}
	public void setReviewState(WorkFlowReviewStages reviewState) {
		this.reviewState = reviewState;
	}
	public RoleMaster getRole() {
		return role;
	}
	public void setRole(RoleMaster role) {
		this.role = role;
	}
	public LoginUsers getUser() {
		return user;
	}
	public void setUser(LoginUsers user) {
		this.user = user;
	}
	public Date getDateOfActivity() {
		return dateOfActivity;
	}
	public void setDateOfActivity(Date dateOfActivity) {
		this.dateOfActivity = dateOfActivity;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isInTheFlow() {
		return inTheFlow;
	}
	public void setInTheFlow(boolean inTheFlow) {
		this.inTheFlow = inTheFlow;
	}
	
	
	
}