package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="VOLUNTEER_PERIOD_CRF_STATUS")
public class VolunteerPeriodCrfStatus extends CommonMaster  implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="VolunteerPeriodCrfStatus_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private Long studyId;
//	@ManyToOne
//	@JoinColumn
	@Transient
	private StudyPeriodMaster period;
	@ManyToOne
	@JoinColumn
	private Volunteer vol;
	private String status = "NOT STARTED"; //  IN PROGRESS, IN REVIEW, COMPLETED
	
	private String startDate = "";
	
	private String withDraw="No"; // Yes
	private String withDrownBy = "";
	private Date withDrownDate;
	private String phaseContinue = "No"; // Continue
	private String allowDateEntry = "Yes"; // No;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public StudyPeriodMaster getPeriod() {
		return period;
	}
	public void setPeriod(StudyPeriodMaster period) {
		this.period = period;
	}
	public Volunteer getVol() {
		return vol;
	}
	public void setVol(Volunteer vol) {
		this.vol = vol;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWithDraw() {
		return withDraw;
	}
	public void setWithDraw(String withDraw) {
		this.withDraw = withDraw;
	}
	public String getWithDrownBy() {
		return withDrownBy;
	}
	public void setWithDrownBy(String withDrownBy) {
		this.withDrownBy = withDrownBy;
	}
	public Date getWithDrownDate() {
		return withDrownDate;
	}
	public void setWithDrownDate(Date withDrownDate) {
		this.withDrownDate = withDrownDate;
	}
	public String getPhaseContinue() {
		return phaseContinue;
	}
	public void setPhaseContinue(String phaseContinue) {
		this.phaseContinue = phaseContinue;
	}
	public String getAllowDateEntry() {
		return allowDateEntry;
	}
	public void setAllowDateEntry(String allowDateEntry) {
		this.allowDateEntry = allowDateEntry;
	}
	
	
}
