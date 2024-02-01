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

@Entity
@Table(name="STUDY_ANALYTE_MASTER")
public class StudyAnalyteMaster extends CommonMaster implements Serializable{

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyAnalyteMaster_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String analyteName;
	private String analyteDesc;
	private String analyteType;
	@ManyToOne
	@JoinColumn
	private StudyMaster studyMaster;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAnalyteName() {
		return analyteName;
	}
	public void setAnalyteName(String analyteName) {
		this.analyteName = analyteName;
	}
	public String getAnalyteDesc() {
		return analyteDesc;
	}
	public void setAnalyteDesc(String analyteDesc) {
		this.analyteDesc = analyteDesc;
	}
	public String getAnalyteType() {
		return analyteType;
	}
	public void setAnalyteType(String analyteType) {
		this.analyteType = analyteType;
	}
	public StudyMaster getStudyMaster() {
		return studyMaster;
	}
	public void setStudyMaster(StudyMaster studyMaster) {
		this.studyMaster = studyMaster;
	}
	
}
