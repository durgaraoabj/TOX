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
@Table(name="VOLUNTEER_PERIOD_CRF")
public class VolunteerPeriodCrf extends CommonMaster  implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="VolunteerPeriodCrf_id_seq", allocationSize=1)
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
	private PeriodCrfs stdCrf;
	
	private long studyId;
	
	private String crfStatus = "NOT STARTED";  
	//'' or NOT STARTED, IN PROGRESS, IN REVIEW , COMPLETED
	//FOR DICREPENCY'S ---- OPEN , ON HOLD , CLOSED
	private String exitCrf = "No"; // Yes
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getExitCrf() {
		return exitCrf;
	}
	public void setExitCrf(String exitCrf) {
		this.exitCrf = exitCrf;
	}
	public String getCrfStatus() {
		return crfStatus;
	}
	public void setCrfStatus(String crfStatus) {
		this.crfStatus = crfStatus;
	}
	public long getStudyId() {
		return studyId;
	}
	public void setStudyId(long studyId) {
		this.studyId = studyId;
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
	public PeriodCrfs getStdCrf() {
		return stdCrf;
	}
	public void setStdCrf(PeriodCrfs stdCrf) {
		this.stdCrf = stdCrf;
	}
	
	
}
