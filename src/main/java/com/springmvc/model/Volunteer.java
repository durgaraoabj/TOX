package com.springmvc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@Table(name="VOLUNTEER")
public class Volunteer extends CommonMaster  implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="Volunteer_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private long studyId;
	private String volId;
	private String volName;
	private String bedNo;
	
	@ManyToOne
	@JoinColumn
	private StudyMaster studyMaster;
	
	@ManyToOne
	@JoinColumn
	private StudySite site;
	
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
	public String getVolId() {
		return volId;
	}
	public void setVolId(String volId) {
		this.volId = volId;
	}
	public String getVolName() {
		return volName;
	}
	public void setVolName(String volName) {
		this.volName = volName;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	@Transient
	private List<StudyPeriodMaster> periods = new ArrayList<StudyPeriodMaster>();


	public List<StudyPeriodMaster> getPeriods() {
		return periods;
	}
	public void setPeriods(List<StudyPeriodMaster> periods) {
		this.periods = periods;
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
	
}
