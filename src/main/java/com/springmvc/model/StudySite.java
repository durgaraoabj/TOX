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
@Table(name="STUDY_SITE")
public class StudySite extends CommonMaster implements Comparable<StudySite>, Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudySite_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn
	private StudyMaster studyMaster;
	private String siteName = "";
	private String protocalId = "";
	private String secondaryIDs;
	private String principalInvestigator = "";
	private String briefSummary;
	private String protocolVerificationIRBApprovalDate;
	private String startDate = "";
	private String endDate = "";
	private int subjects;
	private String facilityName = "";
	private String facilityCity;
	private String facilityState;
	private String facilityZip;
	private String facilityCountry;
	private String facilityContactName;
	private String facilityContactDegree;
	private String facilityContactPhone;
	private String facilityContactEmail;
	private boolean status = true;
	private String state = "NEW"; // NEW, 
	private int siteNo;
	
	@Transient
	private List<Long> usersIds = new ArrayList<Long>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Long> getUsersIds() {
		return usersIds;
	}

	public void setUsersIds(List<Long> usersIds) {
		this.usersIds = usersIds;
	}

	public String getSecondaryIDs() {
		return secondaryIDs;
	}

	public void setSecondaryIDs(String secondaryIDs) {
		this.secondaryIDs = secondaryIDs;
	}

	public String getBriefSummary() {
		return briefSummary;
	}

	public void setBriefSummary(String briefSummary) {
		this.briefSummary = briefSummary;
	}

	public String getProtocolVerificationIRBApprovalDate() {
		return protocolVerificationIRBApprovalDate;
	}

	public void setProtocolVerificationIRBApprovalDate(String protocolVerificationIRBApprovalDate) {
		this.protocolVerificationIRBApprovalDate = protocolVerificationIRBApprovalDate;
	}

	public String getFacilityCity() {
		return facilityCity;
	}

	public void setFacilityCity(String facilityCity) {
		this.facilityCity = facilityCity;
	}

	public String getFacilityState() {
		return facilityState;
	}

	public void setFacilityState(String facilityState) {
		this.facilityState = facilityState;
	}

	public String getFacilityZip() {
		return facilityZip;
	}

	public void setFacilityZip(String facilityZip) {
		this.facilityZip = facilityZip;
	}

	public String getFacilityCountry() {
		return facilityCountry;
	}

	public void setFacilityCountry(String facilityCountry) {
		this.facilityCountry = facilityCountry;
	}

	public String getFacilityContactName() {
		return facilityContactName;
	}

	public void setFacilityContactName(String facilityContactName) {
		this.facilityContactName = facilityContactName;
	}

	public String getFacilityContactDegree() {
		return facilityContactDegree;
	}

	public void setFacilityContactDegree(String facilityContactDegree) {
		this.facilityContactDegree = facilityContactDegree;
	}

	public String getFacilityContactPhone() {
		return facilityContactPhone;
	}

	public void setFacilityContactPhone(String facilityContactPhone) {
		this.facilityContactPhone = facilityContactPhone;
	}

	public String getFacilityContactEmail() {
		return facilityContactEmail;
	}

	public void setFacilityContactEmail(String facilityContactEmail) {
		this.facilityContactEmail = facilityContactEmail;
	}

	public StudyMaster getStudyMaster() {
		return studyMaster;
	}

	public void setStudyMaster(StudyMaster studyMaster) {
		this.studyMaster = studyMaster;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getProtocalId() {
		return protocalId;
	}

	public void setProtocalId(String protocalId) {
		this.protocalId = protocalId;
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

	public String getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(String principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public int getSubjects() {
		return subjects;
	}

	public void setSubjects(int subjects) {
		this.subjects = subjects;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getSiteNo() {
		return siteNo;
	}

	public void setSiteNo(int siteNo) {
		this.siteNo = siteNo;
	}

	@Override
	public int compareTo(StudySite o) {
		return ((Integer)this.getSiteNo()).compareTo((Integer)o.getSiteNo());
	}
	
	
}
