package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.covide.crf.dto.Crf;

@SuppressWarnings("serial")
@Entity
@Table(name="amendment_details")
public class AmendmentDetails extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="AmendmentDetails_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="action")
	private String action;
	@ManyToOne
	@JoinColumn(name="crf_id")
	private Crf crf;
	private String amendmentType = "";
	@ManyToOne
	@JoinColumn(name="obv_id")
	private StdSubGroupObservationCrfs obvId;
	@ManyToOne
	@JoinColumn(name="acclamtizationDataId")
	private StudyAcclamatizationData acclamtizationData;
	@Column(name="study_id")
	private Long studyId;
	@Column(name="study_name")
	private String studyName;
	@Column(name="status")
	private String status="Open";
	
	
	public String getAmendmentType() {
		return amendmentType;
	}
	public void setAmendmentType(String amendmentType) {
		this.amendmentType = amendmentType;
	}
	public StudyAcclamatizationData getAcclamtizationData() {
		return acclamtizationData;
	}
	public void setAcclamtizationData(StudyAcclamatizationData acclamtizationData) {
		this.acclamtizationData = acclamtizationData;
	}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public String getStudyName() {
		return studyName;
	}
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
	public StdSubGroupObservationCrfs getObvId() {
		return obvId;
	}
	public void setObvId(StdSubGroupObservationCrfs obvId) {
		this.obvId = obvId;
	}
	
	
	


}
