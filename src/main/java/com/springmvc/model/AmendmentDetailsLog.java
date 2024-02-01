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
@Table(name="amendment_details_log")
public class AmendmentDetailsLog extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="AmendmentDetailsLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="action")
	private String action;
	@Column(name="crf_id")
	private Long crfId;
	@Column(name="obv_name")
	private String obvName;
	@Column(name="study_id")
	private Long studyId;
	@Column(name="study_name")
	private String studyName;
	@Column(name="status")
	private String status="Open";
	
	@ManyToOne
	@JoinColumn(name="amendment_id")
	private AmendmentDetails amendmentId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Long getCrfId() {
		return crfId;
	}
	public void setCrfId(Long crfId) {
		this.crfId = crfId;
	}
	public String getObvName() {
		return obvName;
	}
	public void setObvName(String obvName) {
		this.obvName = obvName;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public String getStudyName() {
		return studyName;
	}
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public AmendmentDetails getAmendmentId() {
		return amendmentId;
	}
	public void setAmendmentId(AmendmentDetails amendmentId) {
		this.amendmentId = amendmentId;
	}
	
	
	

}
