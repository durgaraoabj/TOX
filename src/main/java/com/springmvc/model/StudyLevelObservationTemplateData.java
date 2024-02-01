package com.springmvc.model;

import java.io.Serializable;
import java.sql.Blob;
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

@SuppressWarnings("serial")
@Entity
@Table(name="study_level_bservation_template_data")
public class StudyLevelObservationTemplateData extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyLevelObservationTemplateData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="study_id")
	private Long studyId;
	@Column(name="group_id")
	private Long groupId;
	@Column(name="sub_group_id")
	private Long subGroupId;
	@Column(name="animal_id")
	private Long animalId;
	@Column(name="file_data")
	private Blob blob;
	@Column(name="review_comments", length=10000)
	private String reviewComments;
	@Column(name="re_assign_comments", length=10000)
	private String reAssignComments;
	@ManyToOne
	@JoinColumn(name="obst_data")
	private ObservationTemplateData obstData;
	@Column(name="status")
	private String status="assigned"; //reassigned , // reviewed
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getSubGroupId() {
		return subGroupId;
	}
	public void setSubGroupId(Long subGroupId) {
		this.subGroupId = subGroupId;
	}
	public Long getAnimalId() {
		return animalId;
	}
	public void setAnimalId(Long animalId) {
		this.animalId = animalId;
	}
	public Blob getBlob() {
		return blob;
	}
	public void setBlob(Blob blob) {
		this.blob = blob;
	}
	public String getReviewComments() {
		return reviewComments;
	}
	public void setReviewComments(String reviewComments) {
		this.reviewComments = reviewComments;
	}
	public String getReAssignComments() {
		return reAssignComments;
	}
	public void setReAssignComments(String reAssignComments) {
		this.reAssignComments = reAssignComments;
	}
	public ObservationTemplateData getObstData() {
		return obstData;
	}
	public void setObstData(ObservationTemplateData obstData) {
		this.obstData = obstData;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
