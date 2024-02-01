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
@Table(name="PERIOD_CRFS")
public class PeriodCrfs extends CommonMaster  implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="PeriodCrfs_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
//	@ManyToOne
//	@JoinColumn
	@Transient
	private StudyPeriodMaster period;
	private Long periodId;
	@Column(name = "is_active")	
	private boolean active = true;//status
	private String  crfName = "";
	private Long crfId; // it is StudyCrf id
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
