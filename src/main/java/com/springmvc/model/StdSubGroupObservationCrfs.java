package com.springmvc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.covide.crf.dto.Crf;

@Entity
@Table(name="Std_SubGroup_ObservationCrfs")
public class StdSubGroupObservationCrfs extends CommonMaster implements Serializable {

	/**
	 * 
	 */
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StdSubGroupObservationCrfs_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String observationName;
	private String observationDesc;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="study_id")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name="crf_id")
	private Crf crf;
	private boolean crfConfig;
	private boolean templeteConfig;
	@ManyToOne
	@JoinColumn(name="subGroupInfo_id")
	private SubGroupInfo subGroupInfo;
	private String days = "";
	private String dayType = "Day";
	private String windowSign ="+/-";
	@Column(name="window_count")
	private int window;
	@Column(name = "is_active")	
	private boolean active = true;//status
	private boolean reviewStatus = true;
	@ManyToOne
	@JoinColumn(name="deptId")
	private DepartmentMaster deptId;
	@Transient
	private List<String> convDates;
	@Transient
	private String groupName;
	@Transient
	private int needToReview = 0;
	@Transient
	private String needTore = "";
	@Transient
	private List<SubGroupAnimalsInfoCrfDataCount> dataCrfs = new ArrayList<>();
	@Transient
	private String color = "#3788d8";
	@Transient
	private List<SubGroupAnimalsInfoAll> animals;
	@Transient
	private boolean allowDataEntry = false;
	@Transient
	private List<StudyTreatmentDataDates> treatmentDates;
	
	private String dataEntryStatus = "No Started"; //No Started, Started, Completed
	private String dataReviewStatus = "No Started"; //No Started, Started, Completed
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDataEntryStatus() {
		return dataEntryStatus;
	}
	public void setDataEntryStatus(String dataEntryStatus) {
		this.dataEntryStatus = dataEntryStatus;
	}
	public String getDataReviewStatus() {
		return dataReviewStatus;
	}
	public void setDataReviewStatus(String dataReviewStatus) {
		this.dataReviewStatus = dataReviewStatus;
	}
	public List<StudyTreatmentDataDates> getTreatmentDates() {
		return treatmentDates;
	}
	public void setTreatmentDates(List<StudyTreatmentDataDates> treatmentDates) {
		this.treatmentDates = treatmentDates;
	}
	public boolean isAllowDataEntry() {
		return allowDataEntry;
	}
	public void setAllowDataEntry(boolean allowDataEntry) {
		this.allowDataEntry = allowDataEntry;
	}
	public String getWindowSign() {
		return windowSign;
	}
	public void setWindowSign(String windowSign) {
		this.windowSign = windowSign;
	}
	public int getWindow() {
		return window;
	}
	public void setWindow(int window) {
		this.window = window;
	}
	public List<SubGroupAnimalsInfoAll> getAnimals() {
		return animals;
	}
	public void setAnimals(List<SubGroupAnimalsInfoAll> animals) {
		this.animals = animals;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(boolean reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public int getNeedToReview() {
		return needToReview;
	}
	public void setNeedToReview(int needToReview) {
		this.needToReview = needToReview;
	}
	public List<SubGroupAnimalsInfoCrfDataCount> getDataCrfs() {
		return dataCrfs;
	}
	public void setDataCrfs(List<SubGroupAnimalsInfoCrfDataCount> dataCrfs) {
		this.dataCrfs = dataCrfs;
	}
	public String getObservationName() {
		return observationName;
	}
	public void setObservationName(String observationName) {
		this.observationName = observationName;
	}
	public String getObservationDesc() {
		return observationDesc;
	}
	public void setObservationDesc(String observationDesc) {
		this.observationDesc = observationDesc;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public SubGroupInfo getSubGroupInfo() {
		return subGroupInfo;
	}
	public void setSubGroupInfo(SubGroupInfo subGroupInfo) {
		this.subGroupInfo = subGroupInfo;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	
	public DepartmentMaster getDeptId() {
		return deptId;
	}
	public void setDeptId(DepartmentMaster deptId) {
		this.deptId = deptId;
	}
	public String getDayType() {
		return dayType;
	}
	public void setDayType(String dayType) {
		this.dayType = dayType;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isCrfConfig() {
		return crfConfig;
	}
	public void setCrfConfig(boolean crfConfig) {
		this.crfConfig = crfConfig;
	}
	public boolean isTempleteConfig() {
		return templeteConfig;
	}
	public void setTempleteConfig(boolean templeteConfig) {
		this.templeteConfig = templeteConfig;
	}
	public String getNeedTore() {
		return needTore;
	}
	public void setNeedTore(String needTore) {
		this.needTore = needTore;
	}
	public List<String> getConvDates() {
		return convDates;
	}
	public void setConvDates(List<String> convDates) {
		this.convDates = convDates;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	
}
