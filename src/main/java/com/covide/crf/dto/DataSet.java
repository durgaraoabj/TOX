package com.covide.crf.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.springmvc.model.CommonMaster;
import com.springmvc.model.StudyMaster;

@Entity
@Table(name = "dataset")
public class DataSet extends CommonMaster implements Serializable{
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="DataSet_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(columnDefinition="TEXT")
	private String name;
	@Column(columnDefinition="TEXT")
	private String description;  // desc
	private String itemStatus = "all";  // all, marked, completed 
	private String status = "active";
	@ManyToOne
	@JoinColumn(name="study")
	private StudyMaster study;
	
	private String metaDataVersion;
	private String metaDataVersionName;
	private String studyODMIdName;
	private String previousMetaDataVersionName;
	@OneToMany(cascade=CascadeType.PERSIST,  mappedBy="dataSetId")
	private List<DataSetPhase> phases;
	

	
	public String getMetaDataVersion() {
		return metaDataVersion;
	}
	public void setMetaDataVersion(String metaDataVersion) {
		this.metaDataVersion = metaDataVersion;
	}
	public String getMetaDataVersionName() {
		return metaDataVersionName;
	}
	public void setMetaDataVersionName(String metaDataVersionName) {
		this.metaDataVersionName = metaDataVersionName;
	}
	public String getStudyODMIdName() {
		return studyODMIdName;
	}
	public void setStudyODMIdName(String studyODMIdName) {
		this.studyODMIdName = studyODMIdName;
	}
	public String getPreviousMetaDataVersionName() {
		return previousMetaDataVersionName;
	}
	public void setPreviousMetaDataVersionName(String previousMetaDataVersionName) {
		this.previousMetaDataVersionName = previousMetaDataVersionName;
	}
	
	
	public List<DataSetPhase> getPhases() {
		return phases;
	}
	public void setPhases(List<DataSetPhase> phases) {
		this.phases = phases;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	
	
}
