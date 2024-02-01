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
import javax.persistence.Transient;

@Entity
@Table(name="STUDY_SITE_PERIOD_CRFS")
public class StudySitePeriodCrfs extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudySitePeriodCrfs_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn
	private StudyMaster studyMaster;
	
	@ManyToOne
	@JoinColumn
	private StudySite site;
	
	@ManyToOne
	@JoinColumn
	private PeriodCrfs periodCrf;
	
//	@ManyToOne
//	@JoinColumn
	@Transient
	private StudyPeriodMaster period;
	private Long periodId;
	@Column(name = "is_active")	
	private boolean active = true;//status
	private String  crfName = "";
	private Long crfId; // it is StudyCrf id
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyMaster getStudyMaster() {
		return studyMaster;
	}
	public void setStudyMaster(StudyMaster studyMaster) {
		this.studyMaster = studyMaster;
	}
	
	
	public StudySite getSite() {
		return site;
	}
	public void setSite(StudySite site) {
		this.site = site;
	}
	public PeriodCrfs getPeriodCrf() {
		return periodCrf;
	}
	public void setPeriodCrf(PeriodCrfs periodCrf) {
		this.periodCrf = periodCrf;
	}
	public StudyPeriodMaster getPeriod() {
		return period;
	}
	public void setPeriod(StudyPeriodMaster period) {
		this.period = period;
	}
	public Long getPeriodId() {
		return periodId;
	}
	public void setPeriodId(Long periodId) {
		this.periodId = periodId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getCrfName() {
		return crfName;
	}
	public void setCrfName(String crfName) {
		this.crfName = crfName;
	}
	public Long getCrfId() {
		return crfId;
	}
	public void setCrfId(Long crfId) {
		this.crfId = crfId;
	}
	
	
}
