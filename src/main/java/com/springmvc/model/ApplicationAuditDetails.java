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
@Table(name="application_audit_dtails")
public class ApplicationAuditDetails extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ApplicationAuditDetails_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster studyId;
	@ManyToOne
	@JoinColumn(name="wfsd_id")
	private WorkFlowStatusDetails wfsdId;
	@Column(name="action")
	private String action;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyMaster getStudyId() {
		return studyId;
	}
	public void setStudyId(StudyMaster studyId) {
		this.studyId = studyId;
	}
	public WorkFlowStatusDetails getWfsdId() {
		return wfsdId;
	}
	public void setWfsdId(WorkFlowStatusDetails wfsdId) {
		this.wfsdId = wfsdId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	

}
