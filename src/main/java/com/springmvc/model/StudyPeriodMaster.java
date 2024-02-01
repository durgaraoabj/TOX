package com.springmvc.model;

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
@Table(name="STUDY_PERIOD_MASTER")
public class StudyPeriodMaster extends CommonMaster implements Comparable<StudyPeriodMaster>{

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyPeriodMaster_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String name;
	private String startDate;
	private String endDate;
	private int periodNo = 0;
	private String type = "Period"; // Screening, Study Assessment, Period, Post Study 
	private String doseDate;
	
	private String status = "No"; // No , Yes, Exit
	private String window = "";
	private int noOfDays;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPeriodNo() {
		return periodNo;
	}

	public void setPeriodNo(int periodNo) {
		this.periodNo = periodNo;
	}

	@ManyToOne
	@JoinColumn
	private StudyMaster studyMaster;
	
	private String crfStatus = ""; //In Progress or In Review or On Hold or Discrepancy Raised or Completed
	
	private boolean crfConfiguation   = false;
	
	@Transient
	private int order;
	public boolean isCrfConfiguation() {
		return crfConfiguation;
	}

	public void setCrfConfiguation(boolean crfConfiguation) {
		this.crfConfiguation = crfConfiguation;
	}

	public String getCrfStatus() {
		return crfStatus;
	}

	public void setCrfStatus(String crfStatus) {
		this.crfStatus = crfStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public StudyMaster getStudyMaster() {
		return studyMaster;
	}

	public void setStudyMaster(StudyMaster studyMaster) {
		this.studyMaster = studyMaster;
	}
	
	@Transient
	private String periodVolCrfStatus = "";


	public String getPeriodVolCrfStatus() {
		return periodVolCrfStatus;
	}

	public void setPeriodVolCrfStatus(String periodVolCrfStatus) {
		this.periodVolCrfStatus = periodVolCrfStatus;
	}
	@Transient
	private VolunteerPeriodCrfStatus vpcs;


	public VolunteerPeriodCrfStatus getVpcs() {
		return vpcs;
	}

	public void setVpcs(VolunteerPeriodCrfStatus vpcs) {
		this.vpcs = vpcs;
	}

	public String getDoseDate() {
		return doseDate;
	}

	public void setDoseDate(String doseDate) {
		this.doseDate = doseDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int compareTo(StudyPeriodMaster o) {
		return ((Integer)this.getPeriodNo()).compareTo((Integer)o.getPeriodNo());
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	@Transient
	private String phaseContinue = "Yes";


	public String getPhaseContinue() {
		return phaseContinue;
	}

	public void setPhaseContinue(String phaseContinue) {
		this.phaseContinue = phaseContinue;
	}

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		this.window = window;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}
	
	
}
