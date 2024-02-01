package com.springmvc.model;

import java.io.Serializable;

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
@Table(name="Subject_Status")
public class SubjectStatus extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SubjectStatus_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn
	private Volunteer vol;
//	@ManyToOne
//	@JoinColumn
	@Transient
	private StudyPeriodMaster period;
	
	@ManyToOne
	@JoinColumn
	private StudyMaster study;
	
	@ManyToOne
	@JoinColumn
	private StudySite site;
	
	private String withDraw="No"; // Yes
	private String phaseContinue = "No"; // Continue
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Volunteer getVol() {
		return vol;
	}
	public void setVol(Volunteer vol) {
		this.vol = vol;
	}
	public StudyPeriodMaster getPeriod() {
		return period;
	}
	public void setPeriod(StudyPeriodMaster period) {
		this.period = period;
	}
	public String getWithDraw() {
		return withDraw;
	}
	public void setWithDraw(String withDraw) {
		this.withDraw = withDraw;
	}
	public String getPhaseContinue() {
		return phaseContinue;
	}
	public void setPhaseContinue(String phaseContinue) {
		this.phaseContinue = phaseContinue;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public StudySite getSite() {
		return site;
	}
	public void setSite(StudySite site) {
		this.site = site;
	}
	
	
}
