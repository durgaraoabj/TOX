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

@SuppressWarnings("serial")
@Entity
@Table(name="study_design_status")
public class StudyDesignStatus extends CommonMaster implements Serializable{
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyDesignStatus_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="studyId")
	private long studyId;
	@Column(name="count")
	private int count=1;
	@ManyToOne
	@JoinColumn(name="status")
	private StatusMaster status;
	
	@Column(name="comments", length=10000)
	private String comments="";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getStudyId() {
		return studyId;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public StatusMaster getStatus() {
		return status;
	}

	public void setStatus(StatusMaster status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	

}
