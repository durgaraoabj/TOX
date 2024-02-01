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
@Table(name="study_level_bservation_template_data_log")
public class StudyLevelObservationTemplateDataLog extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyLevelObservationTemplateDataLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="file_data")
	private Blob blob;
	@ManyToOne
	@JoinColumn(name="slotd")
	private StudyLevelObservationTemplateData slotd;
	@Column(name="review_comments", length=10000)
	private String reviewComments;
	@Column(name="re_assign_comments", length=10000)
	private String reAssignComments;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Blob getBlob() {
		return blob;
	}
	public void setBlob(Blob blob) {
		this.blob = blob;
	}
	public StudyLevelObservationTemplateData getSlotd() {
		return slotd;
	}
	public void setSlotd(StudyLevelObservationTemplateData slotd) {
		this.slotd = slotd;
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
	
	

}
