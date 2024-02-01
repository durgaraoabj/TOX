package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="IpRequest")
public class IpRequest extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="IpRequest_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	private Randomization randomization; 
	@ManyToOne
	private StudyMaster std;
	private int subject;
//	@ManyToOne
	@Transient
	private StudyPeriodMaster period;
	@ManyToOne
	private StudySite site;
	private String testSeq  = "";
	private String status = "REQUESTED"; //REQUESTED or AVAILABLE
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyMaster getStd() {
		return std;
	}
	public void setStd(StudyMaster std) {
		this.std = std;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	public StudyPeriodMaster getPeriod() {
		return period;
	}
	public void setPeriod(StudyPeriodMaster period) {
		this.period = period;
	}
	public String getTestSeq() {
		return testSeq;
	}
	public void setTestSeq(String testSeq) {
		this.testSeq = testSeq;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Randomization getRandomization() {
		return randomization;
	}
	public void setRandomization(Randomization randomization) {
		this.randomization = randomization;
	}
	public StudySite getSite() {
		return site;
	}
	public void setSite(StudySite site) {
		this.site = site;
	}
	
}
